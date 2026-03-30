package com.hanserwei.hannote.biz.context.holer;

import com.alibaba.ttl.TransmittableThreadLocal;

/**
 * 登录用户上下文持有者
 * <p>
 * 利用阿里巴巴 TTL 机制，在当前线程中存储用户信息，实现用户信息在逻辑层级间的透明传递。
 * 避免了在方法调用链中频繁传递用户 ID 等参数。
 * <p>
 * 与普通 ThreadLocal 不同，TTL 可在线程池复用线程的场景下传递上下文。
 * 使用完毕后需要及时清理，避免线程复用导致上下文污染。
 *
 * @author hanserwei
 */
public class LoginUserContextHolder {

    /**
     * 存储当前登录用户 ID 的 TTL
     */
    private static final TransmittableThreadLocal<Long> USER_ID_THREAD_LOCAL = new TransmittableThreadLocal<>();

    /**
     * 设置当前登录用户 ID
     *
     * @param userId 用户 ID
     */
    public static void setUserId(Long userId) {
        USER_ID_THREAD_LOCAL.set(userId);
    }

    /**
     * 从当前作用域中获取用户 ID
     *
     * @return 用户 ID；如果当前不在绑定作用域内，则返回 null
     */
    public static Long getUserId() {
        return USER_ID_THREAD_LOCAL.get();
    }

    /**
     * 移除当前登录用户上下文
     */
    public static void remove() {
        USER_ID_THREAD_LOCAL.remove();
    }
}
