package com.hanserwei.hannote.biz.context.holer;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.hanserwei.framework.constant.GlobalConstants;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 登录用户上下文持有者
 * <p>
 * 利用 ThreadLocal 机制，在当前线程中存储用户信息，实现用户信息在逻辑层级间的透明传递。
 * 避免了在方法调用链中频繁传递用户 ID 等参数。
 *
 * @author hanserwei
 */
public class LoginUserContextHolder {

    /**
     * 初始化 ThreadLocal 变量
     * <p>
     * 初始值为一个空的 HashMap，用于存放当前线程相关的上下文数据
     */
    private static final ThreadLocal<Map<String, Object>> LOGIN_USER_CONTEXT_THREAD_LOCAL
            = TransmittableThreadLocal.withInitial(HashMap::new);

    /**
     * 将用户 ID 设置到当前线程上下文中
     *
     * @param value 用户 ID (通常为 Long 或 String 类型)
     */
    public static void setUserId(Object value) {
        LOGIN_USER_CONTEXT_THREAD_LOCAL.get().put(GlobalConstants.USER_ID, value);
    }

    /**
     * 从当前线程上下文中获取用户 ID
     *
     * @return 转换后的 Long 型用户 ID；如果上下文中不存在，则返回 null
     */
    public static Long getUserId() {
        Object value = LOGIN_USER_CONTEXT_THREAD_LOCAL.get().get(GlobalConstants.USER_ID);
        if (Objects.isNull(value)) {
            return null;
        }
        // 使用 toString() 配合 Long.valueOf()，增加对不同输入类型的容错性
        return Long.valueOf(value.toString());
    }

    /**
     * 移除当前线程的所有上下文数据
     * <p>
     * <b>重要：</b>由于在高并发场景下，线程通常由线程池管理（会被重用），
     * 请求结束前必须调用此方法，否则可能导致【内存泄漏】或【用户信息串户】。
     */
    public static void remove() {
        LOGIN_USER_CONTEXT_THREAD_LOCAL.remove();
    }

}