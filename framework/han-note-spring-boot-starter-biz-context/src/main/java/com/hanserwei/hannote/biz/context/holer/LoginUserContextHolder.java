package com.hanserwei.hannote.biz.context.holer;

import java.util.Objects;

/**
 * 登录用户上下文持有者
 * <p>
 * 基于 ScopedValue 在当前调用作用域内存储用户信息，避免在方法调用链中频繁传递用户 ID。
 *
 * @author hanserwei
 */
public class LoginUserContextHolder {

    /**
     * 存储当前登录用户 ID 的 ScopedValue
     */
    private static final ScopedValue<Long> USER_ID_SCOPED_VALUE = ScopedValue.newInstance();

    private LoginUserContextHolder() {
    }

    /**
     * 在当前作用域内绑定用户 ID 并执行任务
     *
     * @param userId 用户 ID
     * @param runnable 待执行任务
     */
    public static void runWithUserId(Long userId, Runnable runnable) {
        Objects.requireNonNull(runnable, "runnable must not be null");
        if (Objects.isNull(userId)) {
            runnable.run();
            return;
        }
        ScopedValue.where(USER_ID_SCOPED_VALUE, userId).run(runnable);
    }

    /**
     * 在当前作用域内绑定用户 ID 并执行任务
     *
     * @param userId 用户 ID
     * @param callable 待执行任务
     * @return 任务执行结果
     * @param <T> 返回值类型
     * @param <X> 异常类型
     * @throws X callable 抛出的异常
     */
    public static <T, X extends Throwable> T callWithUserId(Long userId,
                                                            ScopedValue.CallableOp<? extends T, X> callable) throws X {
        Objects.requireNonNull(callable, "callable must not be null");
        if (Objects.isNull(userId)) {
            return callable.call();
        }
        return ScopedValue.where(USER_ID_SCOPED_VALUE, userId).call(callable);
    }

    /**
     * 从当前作用域中获取用户 ID
     *
     * @return 用户 ID；如果当前不在绑定作用域内，则返回 null
     */
    public static Long getUserId() {
        return USER_ID_SCOPED_VALUE.isBound() ? USER_ID_SCOPED_VALUE.get() : null;
    }
}
