package com.hanserwei.hannote.auth.constant;

/**
 * @author hanserwei
 */
public class RedisKeyConstants {

    /**
     * 验证码 KEY 前缀
     */
    private static final String VERIFICATION_CODE_KEY_PREFIX = "verification_code:";

    /**
     * 更新密码验证码 KEY 前缀
     */
    private static final String UPDATE_PASSWORD_VERIFICATION_CODE_KEY_PREFIX = "update_password_verification_code:";

    /**
     * 小憨书全局 ID 生成器 KEY
     */
    public static final String HANNOTE_ID_GENERATOR_KEY = "hannote.id.generator";

    /**
     * 用户角色数据 KEY 前缀
     */
    private static final String USER_ROLES_KEY_PREFIX = "user:roles:";

    /**
     * 角色对应的权限集合 KEY 前缀
     */
    private static final String ROLE_PERMISSIONS_KEY_PREFIX = "role:permissions:";

    /**
     * 构建验证码 KEY
     *
     * @param phone 手机号
     * @return 验证码Key
     */
    public static String buildVerificationCodeKey(String phone) {
        return VERIFICATION_CODE_KEY_PREFIX + phone;
    }

    /**
     * 构建更新密码验证码 KEY
     *
     * @param phone 手机号
     * @return 验证码Key
     */
    public static String buildUpdatePasswordVerificationCodeKey(String phone) {
        return UPDATE_PASSWORD_VERIFICATION_CODE_KEY_PREFIX + phone;
    }

    /**
     * 构建用户-角色 Key
     *
     * @param userId 手机号
     * @return 角色 Key
     */
    public static String buildUserRoleKey(String userId) {
        return USER_ROLES_KEY_PREFIX + userId;
    }

    /**
     * 构建角色对应的权限集合 KEY
     *
     * @param roleKey 角色 ID
     * @return 角色对应的权限集合 Key
     */
    public static String buildRolePermissionsKey(String roleKey) {
        return ROLE_PERMISSIONS_KEY_PREFIX + roleKey;
    }
}