package com.hanserwei.framework.utils;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * 参数校验工具类
 *
 * @author hanserwei
 */
public final class ParamUtils {

    private ParamUtils() {
    }

    // ============================== 昵称校验常量 ==============================
    private static final int NICK_NAME_MIN_LENGTH = 2;
    private static final int NICK_NAME_MAX_LENGTH = 24;
    private static final Pattern NICK_NAME_SPECIAL_CHAR_PATTERN = Pattern.compile("[!@#$%^&*(),.?\":{}|<>]");

    // ============================== 小憨书号校验常量 ==============================
    private static final int HAN_NOTE_ID_MIN_LENGTH = 6;
    private static final int HAN_NOTE_ID_MAX_LENGTH = 15;
    private static final Pattern HAN_NOTE_ID_PATTERN = Pattern.compile("^[a-zA-Z0-9_]+$");

    // ============================== 密码校验常量 ==============================
    private static final int PASSWORD_MIN_LENGTH = 6;
    private static final int PASSWORD_MAX_LENGTH = 20;
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[a-zA-Z])(?=.*\\d).+$");

    // ============================== 手机号校验常量 ==============================
    private static final Pattern PHONE_PATTERN = Pattern.compile("^1[3-9]\\d{9}$");

    // ============================== 邮箱校验常量 ==============================
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");

    // ============================== 昵称校验 ==============================

    /**
     * 校验昵称是否合法
     * <p>
     * 昵称长度必须在 2-24 个字符之间，且不能包含特殊字符 !@#$%^&*(),.?":{}|&lt;&gt;
     *
     * @param nickname 昵称，不允许为空
     * @return 如果昵称合法返回 true，否则返回 false
     * @throws NullPointerException 如果 nickname 为 null
     */
    public static boolean checkNickname(String nickname) {
        Objects.requireNonNull(nickname, "昵称不能为空");

        int length = nickname.length();
        if (length < NICK_NAME_MIN_LENGTH || length > NICK_NAME_MAX_LENGTH) {
            return false;
        }

        return !NICK_NAME_SPECIAL_CHAR_PATTERN.matcher(nickname).find();
    }

    // ============================== 小憨书号校验 ==============================

    /**
     * 校验小憨书号是否合法
     * <p>
     * 小憨书号长度必须在 6-15 个字符之间，且只能包含字母、数字和下划线
     *
     * @param hanNoteId 小憨书号，不允许为空
     * @return 如果小憨书号合法返回 true，否则返回 false
     * @throws NullPointerException 如果 hanNoteId 为 null
     */
    public static boolean checkHanNoteId(String hanNoteId) {
        Objects.requireNonNull(hanNoteId, "小憨书号不能为空");

        int length = hanNoteId.length();
        if (length < HAN_NOTE_ID_MIN_LENGTH || length > HAN_NOTE_ID_MAX_LENGTH) {
            return false;
        }

        return HAN_NOTE_ID_PATTERN.matcher(hanNoteId).matches();
    }

    // ============================== 密码校验 ==============================

    /**
     * 校验密码是否合法
     * <p>
     * 密码长度必须在 6-20 个字符之间，且必须同时包含字母和数字
     *
     * @param password 密码，不允许为空
     * @return 如果密码合法返回 true，否则返回 false
     * @throws NullPointerException 如果 password 为 null
     */
    public static boolean checkPassword(String password) {
        Objects.requireNonNull(password, "密码不能为空");

        int length = password.length();
        if (length < PASSWORD_MIN_LENGTH || length > PASSWORD_MAX_LENGTH) {
            return false;
        }

        return PASSWORD_PATTERN.matcher(password).matches();
    }

    // ============================== 手机号校验 ==============================

    /**
     * 校验手机号是否合法
     * <p>
     * 手机号必须为 11 位数字，且以 1 开头，第二位为 3-9
     *
     * @param phone 手机号，不允许为空
     * @return 如果手机号合法返回 true，否则返回 false
     * @throws NullPointerException 如果 phone 为 null
     */
    public static boolean checkPhone(String phone) {
        Objects.requireNonNull(phone, "手机号不能为空");

        return PHONE_PATTERN.matcher(phone).matches();
    }

    // ============================== 邮箱校验 ==============================

    /**
     * 校验邮箱是否合法
     *
     * @param email 邮箱，不允许为空
     * @return 如果邮箱合法返回 true，否则返回 false
     * @throws NullPointerException 如果 email 为 null
     */
    public static boolean checkEmail(String email) {
        Objects.requireNonNull(email, "邮箱不能为空");

        return EMAIL_PATTERN.matcher(email).matches();
    }

    // ============================== 字符串长度校验 ==============================

    /**
     * 校验字符串长度是否在指定范围内
     *
     * @param str      待校验的字符串，不允许为空
     * @param maxLength 最大长度，必须大于等于 0
     * @return 如果字符串长度不超过 maxLength 返回 true，否则返回 false
     * @throws NullPointerException     如果 str 为 null
     * @throws IllegalArgumentException 如果 maxLength 小于 0
     */
    public static boolean checkLength(String str, int maxLength) {
        Objects.requireNonNull(str, "字符串不能为空");

        if (maxLength < 0) {
            throw new IllegalArgumentException("最大长度不能为负数");
        }

        return str.length() <= maxLength;
    }

    /**
     * 校验字符串长度是否在指定范围内
     *
     * @param str      待校验的字符串，不允许为空
     * @param minLength 最小长度，必须大于等于 0
     * @param maxLength 最大长度，必须大于等于 minLength
     * @return 如果字符串长度在范围内返回 true，否则返回 false
     * @throws NullPointerException     如果 str 为 null
     * @throws IllegalArgumentException 如果参数不合法
     */
    public static boolean checkLength(String str, int minLength, int maxLength) {
        Objects.requireNonNull(str, "字符串不能为空");

        if (minLength < 0) {
            throw new IllegalArgumentException("最小长度不能为负数");
        }
        if (maxLength < minLength) {
            throw new IllegalArgumentException("最大长度不能小于最小长度");
        }

        int length = str.length();
        return length >= minLength && length <= maxLength;
    }

    // ============================== 非空校验 ==============================

    /**
     * 校验字符串是否不为空（不为 null 且不为空字符串）
     *
     * @param str 待校验的字符串
     * @return 如果字符串不为空返回 true，否则返回 false
     */
    public static boolean isNotEmpty(String str) {
        return str != null && !str.isEmpty();
    }

    /**
     * 校验字符串是否不为空白（不为 null 且去除空白字符后不为空）
     *
     * @param str 待校验的字符串
     * @return 如果字符串不为空白返回 true，否则返回 false
     */
    public static boolean isNotBlank(String str) {
        return str != null && !str.isBlank();
    }
}
