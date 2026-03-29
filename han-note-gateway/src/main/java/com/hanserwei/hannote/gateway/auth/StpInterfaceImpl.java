package com.hanserwei.hannote.gateway.auth;

import cn.dev33.satoken.stp.StpInterface;
import cn.hutool.core.collection.CollUtil;
import com.hanserwei.hannote.gateway.constant.RedisKeyConstants;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.json.JsonMapper;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

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
     * 预定义 List<String> 类型，避免重复调用 getTypeFactory
     */
    private static final JavaType LIST_STRING_TYPE = JsonMapper.builder().build().getTypeFactory()
            .constructCollectionType(List.class, String.class);

    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        log.info("==> 获取用户权限列表, loginId: {}", loginId);

        // 1. 获取用户角色列表
        List<String> userRoles = getRoleList(loginId, loginType);
        if (CollUtil.isEmpty(userRoles)) {
            return Collections.emptyList();
        }

        // 2. 批量构建角色对应的权限缓存 Key
        List<String> rolePermissionsKeys = userRoles.stream()
                .map(RedisKeyConstants::buildRolePermissionsKey)
                .toList();

        // 3. 批量从 Redis 获取 JSON 字符串并展平 (Flatten)
        return Optional.ofNullable(redisTemplate.opsForValue().multiGet(rolePermissionsKeys))
                .orElse(Collections.emptyList())
                .stream()
                .filter(StringUtils::isNotBlank)
                .flatMap(this::parseJsonToStream)
                .distinct() // 去重（多个角色可能拥有相同权限）
                .toList();
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        log.info("==> 获取用户角色列表, loginId: {}", loginId);
        String userRoleKey = RedisKeyConstants.buildUserRoleKey(loginId.toString());
        String json = redisTemplate.opsForValue().get(userRoleKey);

        return parseJsonToStream(json).toList();
    }

    /**
     * 将 JSON 字符串安全解析为 Stream
     */
    private Stream<String> parseJsonToStream(String json) {
        if (StringUtils.isBlank(json)) {
            return Stream.empty();
        }
        try {
            List<String> list = jsonMapper.readValue(json, LIST_STRING_TYPE);
            return list.stream();
        } catch (Exception e) {
            log.warn("解析权限/角色 JSON 失败: {}", json, e);
            return Stream.empty();
        }
    }
}