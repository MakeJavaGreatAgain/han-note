package com.hanserwei.hannote.gateway.exception;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import com.hanserwei.framework.response.Response;
import com.hanserwei.hannote.gateway.enums.ResponseCodeEnum;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.webflux.error.ErrorWebExceptionHandler;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import tools.jackson.databind.json.JsonMapper;

/**
 * 全局异常处理器，用于统一处理 WebFlux 应用中的异常
 * 实现 ErrorWebExceptionHandler 接口，拦截所有未处理的异常并返回标准化响应
 *
 * @author hanserwei
 */
@Component
@Slf4j
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {

    @Resource
    private JsonMapper jsonMapper;

    @Override
    @NonNull
    public Mono<Void> handle(@NonNull ServerWebExchange exchange, @NonNull Throwable ex) {
        ServerHttpResponse response = exchange.getResponse();
        log.error("==> 全局异常捕获: ", ex);

        // 使用 Switch 表达式统一处理异常分支并返回响应结果
        Response<?> result = switch (ex) {
            case NotLoginException e -> {
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                yield Response.fail(ResponseCodeEnum.UNAUTHORIZED.getErrorCode(), ex.getMessage());
            }
            case NotPermissionException e -> {
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                yield Response.fail(ResponseCodeEnum.UNAUTHORIZED.getErrorCode(), ResponseCodeEnum.UNAUTHORIZED.getErrorMessage());
            }
            default -> {
                // 对于其他未知异常，通常返回 500 状态码
                response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
                yield Response.fail(ResponseCodeEnum.SYSTEM_ERROR);
            }
        };

        // 设置响应头并写回数据
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        return response.writeWith(Mono.fromSupplier(() -> {
            DataBufferFactory bufferFactory = response.bufferFactory();
            try {
                return bufferFactory.wrap(jsonMapper.writeValueAsBytes(result));
            } catch (Exception e) {
                log.error("响应体序列化失败", e);
                return bufferFactory.wrap(new byte[0]);
            }
        }));
    }
}