package com.hanserwei.hannote.auth.config;

import com.hanserwei.hannote.biz.context.decorator.LoginUserContextTaskDecorator;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.VirtualThreadTaskExecutor;

import java.util.concurrent.Executor;

/**
 * @author hanserwei
 */
@Configuration
public class ThreadPoolConfig {

    @Resource
    private LoginUserContextTaskDecorator loginUserContextTaskDecorator;

    @Bean(name = "authLoginExecutor")
    public Executor authLoginExecutor() {
        return buildContextAwareVirtualExecutor("AuthExecutor-");
    }

    @Bean(name = "authUpdatePasswordExecutor")
    public Executor authUpdatePasswordExecutor() {
        return buildContextAwareVirtualExecutor("AuthExecutor2-");
    }

    private Executor buildContextAwareVirtualExecutor(String threadNamePrefix) {
        VirtualThreadTaskExecutor executor = new VirtualThreadTaskExecutor(threadNamePrefix);
        return command -> executor.execute(loginUserContextTaskDecorator.decorate(command));
    }
}
