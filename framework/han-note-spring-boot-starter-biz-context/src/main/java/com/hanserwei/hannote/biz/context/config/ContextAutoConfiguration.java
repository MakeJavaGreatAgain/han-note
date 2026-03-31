package com.hanserwei.hannote.biz.context.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hanserwei.hannote.biz.context.decorator.LoginUserContextTaskDecorator;
import com.hanserwei.hannote.biz.context.filter.HeaderUserId2ContextFilter;
import com.hanserwei.hannote.biz.context.interceptor.UserIdTransmitRestClientCustomizer;

/**
 * @author hanserwei
 */
@AutoConfiguration
public class ContextAutoConfiguration {

    @Bean
    public HeaderUserId2ContextFilter headerUserId2ContextFilter() {
        return new HeaderUserId2ContextFilter();
    }

    @Bean
    public LoginUserContextTaskDecorator loginUserContextTaskDecorator() {
        return new LoginUserContextTaskDecorator();
    }

    @Configuration
    @ConditionalOnClass(name = "org.springframework.boot.restclient.RestClientCustomizer")
    static class RestClientConfiguration {

        @Bean
        public UserIdTransmitRestClientCustomizer userIdTransmitRestClientCustomizer() {
            return new UserIdTransmitRestClientCustomizer();
        }
    }
}
