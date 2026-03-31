package com.hanserwei.hannote.biz.context.filter;

import java.io.IOException;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jspecify.annotations.NonNull;
import org.springframework.web.filter.OncePerRequestFilter;

import com.hanserwei.framework.constant.GlobalConstants;
import com.hanserwei.hannote.biz.context.holer.LoginUserContextHolder;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 提取请求头中的用户 ID 并绑定到 ScopedValue 作用域中，以方便后续使用
 *
 * @author hanserwei
 */
@Slf4j
public class HeaderUserId2ContextFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain chain) throws ServletException, IOException {

        // 从请求头中获取用户 ID
        String userId = request.getHeader(GlobalConstants.USER_ID);

        log.info("==> HeaderUserId2ContextFilter, 用户 ID: {}", userId);

        // 判断请求头中是否存在用户 ID
        if (StringUtils.isBlank(userId)) {
            // 若为空，则直接放行
            chain.doFilter(request, response);
            return;
        }

        // 如果 header 中存在 userId，则绑定到 ScopedValue 作用域中
        log.info("=====> 绑定 userId 到 ScopedValue 作用域，用户 ID: {}", userId);
        Long userIdLong = Long.valueOf(userId);

        try {
            LoginUserContextHolder.callWithUserId(userIdLong, () -> {
                chain.doFilter(request, response);
                return null;
            });
        } catch (ServletException | IOException e) {
            throw e;
        } catch (Exception e) {
            throw new ServletException("绑定登录用户上下文失败", e);
        }

        log.info("=====> ScopedValue 作用域结束，userId: {}", userId);
    }
}
