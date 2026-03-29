package com.hanserwei.hannote.gateway.auth;

import cn.dev33.satoken.stp.StpInterface;
import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Lists;
import com.hanserwei.hannote.gateway.constant.RedisKeyConstants;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;

/**
 * @author hanserwei
 */
@Component
@Slf4j
public class StpInterfaceImpl implements StpInterface {

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Resource
    private JsonMapper jsonMapper;

    /**
     * 获取用户权限
     *
     * @param loginId   用户 ID
     * @param loginType 登录类型
     * @return 权限列表
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        // 返回此 loginId 拥有的权限列表
        log.info("==> 获取用户权限列表, loginId: {}", loginId);

        //构建用户角色redisKey
        String userRoleKey = RedisKeyConstants.buildUserRoleKey(loginId.toString());

        // 获取用户角色集合
        String userRolesValue = redisTemplate.opsForValue().get(userRoleKey);

        if (StringUtils.isBlank(userRolesValue)) {
            return null;
        }

        // 将 JSON 字符串转换为 List<String> 角色集合
        List<String> userRoleKeys = jsonMapper.convertValue(userRolesValue, jsonMapper.getTypeFactory().constructCollectionType(List.class, String.class));

        if (CollUtil.isNotEmpty(userRoleKeys)) {
            // 查询这些角色对应的权限
            // 构建 角色-权限 Redis Key 集合
            List<String> rolePermissionsKeys = userRoleKeys.stream()
                    .map(RedisKeyConstants::buildRolePermissionsKey)
                    .toList();

            // 通过 key 集合批量查询权限，提升查询性能。
            List<String> rolePermissionsValues = redisTemplate.opsForValue().multiGet(rolePermissionsKeys);

            if (CollUtil.isNotEmpty(rolePermissionsValues)) {
                List<String> permissions = Lists.newArrayList();

                // 遍历所有角色的权限集合，统一添加到 permissions 集合中
                rolePermissionsValues.forEach(jsonValue -> {
                    // 将 JSON 字符串转换为 List<String> 权限集合
                    List<String> rolePermissions = jsonMapper.convertValue(jsonValue, jsonMapper.getTypeFactory().constructCollectionType(List.class, String.class));
                    permissions.addAll(rolePermissions);
                });

                // 返回此用户所拥有的权限
                return permissions;
            }
        }
        return null;
    }

    /**
     * 获取用户角色
     *
     * @param loginId   用户 ID
     * @param loginType 登录类型
     * @return 角色列表
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        log.info("==> 获取用户角色列表, loginId: {}", loginId);

        String userRoleKey = RedisKeyConstants.buildUserRoleKey(loginId.toString());

        // 根据用户 ID ，从 Redis 中获取该用户的角色集合
        String useRolesValue = redisTemplate.opsForValue().get(userRoleKey);

        if (StringUtils.isBlank(useRolesValue)) {
            return null;
        }

        // 将 JSON 字符串转换为 List<String> 集合
        return jsonMapper.convertValue(useRolesValue, jsonMapper.getTypeFactory().constructCollectionType(List.class, String.class));
    }

}