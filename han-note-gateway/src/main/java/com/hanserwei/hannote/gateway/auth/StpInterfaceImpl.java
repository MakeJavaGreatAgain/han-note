package com.hanserwei.hannote.gateway.auth;

import cn.dev33.satoken.stp.StpInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * @author hanserwei
 */
@Component
@Slf4j
public class StpInterfaceImpl implements StpInterface {

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
        log.info("## 获取用户权限列表, loginId: {}", loginId);

        // todo 从 redis 获取

        return Collections.emptyList();
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
        log.info("## 获取用户角色列表, loginId: {}", loginId);

        // 返回此 loginId 拥有的角色列表
        // todo 从 redis 获取

        return Collections.emptyList();
    }

}