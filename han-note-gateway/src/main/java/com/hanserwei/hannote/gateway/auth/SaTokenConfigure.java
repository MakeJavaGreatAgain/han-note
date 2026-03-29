package com.hanserwei.hannote.gateway.auth;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.NotRoleException;
import cn.dev33.satoken.reactor.filter.SaReactorFilter;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author hanserwei
 */
@Configuration
public class SaTokenConfigure {
    /**
     * 注册 Sa-Token 全局过滤器
     *
     * @return SaReactorFilter
     */
    @Bean
    public SaReactorFilter getSaReactorFilter() {
        return new SaReactorFilter()
                // 拦截地址
                .addInclude("/**")
                // 鉴权方法：每次访问进入
                .setAuth(obj -> {
                    // 登录校验
                    SaRouter.match("/**") // 拦截所有路由
                            .notMatch("/auth/user/login") // 排除登录接口
                            .notMatch("/auth/verification/code/login/send") // 排除验证码发送接口
                            .check(StpUtil::checkLogin); // 校验是否登录

                    // 权限认证 -- 不同模块，校验不同权限
                    // SaRouter.match("/user/**", r -> StpUtil.checkPermission("user"));
                    // SaRouter.match("/admin/**", r -> StpUtil.checkPermission("admin"));
                    // SaRouter.match("/goods/**", r -> StpUtil.checkPermission("goods"));
                    // SaRouter.match("/orders/**", r -> StpUtil.checkPermission("orders"));

                    // 更多匹配 ...
                })
                // 异常处理方法：每次setAuth函数出现异常时进入
                .setError(e -> {
                    throw switch (e) {
                        case NotLoginException nle -> nle;
                        case NotPermissionException npe -> npe;
                        case NotRoleException nre -> new NotPermissionException(nre.getMessage());
                        default -> new RuntimeException(e.getMessage(), e);
                    };
                });
    }
}