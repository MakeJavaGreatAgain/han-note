package com.hanserwei.hannote.auth.model.vo.user;

import com.hanserwei.framework.validator.PhoneNumber;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * @author hanserwei
 */
public record UserLoginReqVO(
        /*
         * 手机号
         */
        @NotBlank(message = "手机号不能为空")
        @PhoneNumber
        String phone,

        /*
         * 密码
         */
        String password,

        /*
         * 验证码
         */
        String code,

        /*
         * 登录类型：
         * 1：手机号验证码
         * 2：账号密码
         */
        @NotNull(message = "登录类型不能为空")
        Integer type
) {
}
