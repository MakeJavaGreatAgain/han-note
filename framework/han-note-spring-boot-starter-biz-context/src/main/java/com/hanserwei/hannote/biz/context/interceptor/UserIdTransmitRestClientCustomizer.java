package com.hanserwei.hannote.biz.context.interceptor;

import java.util.Objects;

import org.springframework.boot.restclient.RestClientCustomizer;
import org.springframework.web.client.RestClient;

import com.hanserwei.framework.constant.GlobalConstants;
import com.hanserwei.hannote.biz.context.holer.LoginUserContextHolder;

/**
 * 为 HTTP Interface Client 透传用户 ID 请求头
 *
 * @author hanserwei
 */
public class UserIdTransmitRestClientCustomizer implements RestClientCustomizer {

    @Override
    public void customize(RestClient.Builder restClientBuilder) {
        restClientBuilder.requestInterceptor((request, body, execution) -> {
            Long userId = LoginUserContextHolder.getUserId();
            if (Objects.nonNull(userId) && Objects.isNull(request.getHeaders().getFirst(GlobalConstants.USER_ID))) {
                request.getHeaders().set(GlobalConstants.USER_ID, String.valueOf(userId));
            }
            return execution.execute(request, body);
        });
    }
}
