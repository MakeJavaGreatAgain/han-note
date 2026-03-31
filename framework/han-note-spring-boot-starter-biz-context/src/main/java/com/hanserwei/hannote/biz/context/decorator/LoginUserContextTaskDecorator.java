package com.hanserwei.hannote.biz.context.decorator;

import org.jspecify.annotations.NonNull;
import org.springframework.core.task.TaskDecorator;

import com.hanserwei.hannote.biz.context.holer.LoginUserContextHolder;

/**
 * 将当前作用域内的登录用户上下文桥接到异步任务执行线程
 *
 * @author hanserwei
 */
public class LoginUserContextTaskDecorator implements TaskDecorator {

    @Override
    @NonNull
    public Runnable decorate(@NonNull Runnable runnable) {
        Long userId = LoginUserContextHolder.getUserId();
        return () -> LoginUserContextHolder.runWithUserId(userId, runnable);
    }
}
