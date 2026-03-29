package com.hanserwei.hannote.biz.context.holer;

/**
 * 登录用户上下文持有者
 * <p>
 * 利用 JDK {@link ScopedValue} 机制，在当前作用域中存储用户信息，实现用户信息在逻辑层级间的透明传递。
 * 避免了在方法调用链中频繁传递用户 ID 等参数。
 * <p>
 * 与 ThreadLocal 不同，ScopedValue 是不可变的且与作用域绑定，作用域结束后自动释放，
 * 不存在内存泄漏和线程间串户的风险。在结构化并发（{@code StructuredTaskScope}）中，
 * ScopedValue 会自动传播到子线程。
 *
 * @author hanserwei
 */
public class LoginUserContextHolder {

    /**
     * 存储当前登录用户 ID 的 ScopedValue
     */
    private static final ScopedValue<Long> USER_ID_SCOPED_VALUE = ScopedValue.newInstance();

    /**
     * 在指定用户 ID 的作用域内执行操作（支持受检异常）
     * <p>
     * 用于 Servlet Filter 等需要抛出受检异常的场景。
     *
     * @param userId 用户 ID
     * @param op     要执行的操作
     * @param <R>    返回值类型
     * @param <X>    异常类型
     * @return op 的返回值
     * @throws X op 抛出的异常
     */
    public static <R, X extends Throwable> R callWithUserId(Long userId, ScopedValue.CallableOp<R, X> op) throws X {
        return ScopedValue.where(USER_ID_SCOPED_VALUE, userId).call(op);
    }

    /**
     * 在指定用户 ID 的作用域内执行操作
     *
     * @param userId   用户 ID
     * @param runnable 要执行的操作
     */
    public static void runWithUserId(Long userId, Runnable runnable) {
        ScopedValue.where(USER_ID_SCOPED_VALUE, userId).run(runnable);
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
