package com.hanserwei.hannote.auth.runner;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hanserwei.framework.utils.JsonUtils;
import com.hanserwei.framework.constant.RedisKeyConstants;


import com.hanserwei.hannote.user.biz.domain.dataobject.PermissionDO;
import com.hanserwei.hannote.user.biz.domain.dataobject.RoleDO;
import com.hanserwei.hannote.user.biz.domain.dataobject.RolePermissionRelDO;
import com.hanserwei.hannote.user.biz.domain.mapper.PermissionDOMapper;
import com.hanserwei.hannote.user.biz.domain.mapper.RoleDOMapper;
import com.hanserwei.hannote.user.biz.domain.mapper.RolePermissionRelDOMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author hanserwei
 */
@Component
@Slf4j
public class PushRolePermissions2RedisRunner implements ApplicationRunner {

    @Resource
    private RedisTemplate<String, String> redisTemplate;
    @Resource
    private RoleDOMapper roleDOMapper;
    @Resource
    private PermissionDOMapper permissionDOMapper;
    @Resource
    private RolePermissionRelDOMapper rolePermissionRelDOMapper;

    private static final String PUSH_PERMISSION_FLAG = "push.permission.flag";

    @Override
    public void run(@NonNull ApplicationArguments args) {
        log.info("==> 服务启动，开始同步角色权限数据到 Redis 中...");
        try {
            // 是否能够同步数据: 原子操作，只有在键 PUSH_PERMISSION_FLAG 不存在时，才会设置该键的值为 "1"，并设置过期时间为 1 天
            boolean canPushed = redisTemplate.opsForValue().setIfAbsent(PUSH_PERMISSION_FLAG, "1", 1, TimeUnit.DAYS);

            // 如果无法同步权限数据
            if (!canPushed) {
                log.warn("==> 角色权限数据已经同步至 Redis 中，不再同步...");
                return;
            }
            // 1. 查询所有启用的角色列表
            List<RoleDO> roles = roleDOMapper.selectEnabledList();
            if (CollUtil.isNotEmpty(roles)) {
                // 2. 提取所有角色 ID 集合
                List<Long> roleIds = roles.stream().map(RoleDO::getId).toList();
                
                // 3. 根据角色 ID 批量查询角色与权限的关联关系
                List<RolePermissionRelDO> rolePermissionRels = rolePermissionRelDOMapper.selectByRoleIds(roleIds);
                
                // 4. 将关联关系转换为：角色 ID -> 权限 ID 列表 的映射
                Map<Long, List<Long>> roleIdToPermissionIds = rolePermissionRels.stream().collect(
                        Collectors.groupingBy(RolePermissionRelDO::getRoleId,
                                Collectors.mapping(RolePermissionRelDO::getPermissionId, Collectors.toList()))
                );
                
                // 5. 查询所有应用内启用的权限列表
                List<PermissionDO> permissions = permissionDOMapper.selectAppEnabledList();
                
                // 6. 将权限列表转换为：权限 ID -> 权限对象 的映射，便于后续快速查找
                Map<Long, PermissionDO> permissionIdToPermission = permissions.stream().collect(
                        Collectors.toMap(PermissionDO::getId, permission -> permission)
                );
                
                // 7. 构建最终结果：角色 ID -> 权限对象列表
                Map<String, List<String>> roleIdToPermissions = Maps.newHashMap();
                roles.forEach(role -> {
                    Long roleId = role.getId();
                    String roleKey = role.getRoleKey();
                    // 获取该角色关联的所有权限 ID
                    List<Long> permissionIds = roleIdToPermissionIds.get(roleId);
                    if (CollUtil.isNotEmpty(permissionIds)) {
                        List<String> permissionKeys = Lists.newArrayList();
                        // 遍历权限 ID，从映射中获取具体的权限对象
                        permissionIds.forEach(permissionId -> {
                            PermissionDO permission = permissionIdToPermission.get(permissionId);
                            // 过滤掉可能不存在的权限数据
                            if (Objects.nonNull(permission)) {
                                permissionKeys.add(permission.getPermissionKey());
                            }
                        });
                        roleIdToPermissions.put(roleKey, permissionKeys);
                    }
                });
                
                // 8. 将每个角色的权限列表序列化并存储到 Redis
                roleIdToPermissions.forEach((roleKey, permissionList) -> {
                    String key = RedisKeyConstants.buildRolePermissionsKey(roleKey);
                    redisTemplate.opsForValue().set(key, JsonUtils.toJsonString(permissionList));
                });
            }
            log.info("==> 服务启动，成功同步角色权限数据到 Redis 中...");
        } catch (Exception e) {
            log.error("==> 同步角色权限数据到 Redis 中失败: ", e);
        }
    }
}