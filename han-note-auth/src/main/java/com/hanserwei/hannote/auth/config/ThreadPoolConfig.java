package com.hanserwei.hannote.auth.config;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.VirtualThreadTaskExecutor;

/**
 * @author hanserwei
 */
@Configuration
public class ThreadPoolConfig {

    @Bean(name = "authLoginExecutor")
    public Executor authLoginExecutor() {
        return new VirtualThreadTaskExecutor("AuthExecutor-");
    }

    @Bean(name = "authUpdatePasswordExecutor")
    public Executor authUpdatePasswordExecutor() {
        return new VirtualThreadTaskExecutor("AuthExecutor2-");
    }
}
