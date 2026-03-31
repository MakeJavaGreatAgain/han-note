package com.hanserwei.hannote.biz.context.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

import com.hanserwei.hannote.biz.context.decorator.LoginUserContextTaskDecorator;
import com.hanserwei.hannote.biz.context.filter.HeaderUserId2ContextFilter;

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
}
