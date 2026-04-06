package com.hanserwei.hannote.user.biz.model.vo;

import jakarta.validation.constraints.NotBlank;

/**
 * @author hanserwei
 */
public record UpdatePasswordReqVO(
        @NotBlank(message = "新密码不能为空")
        String newPassword,
        @NotBlank(message = "手机号不能为空")
        String phone,
        @NotBlank(message = "验证码不能为空")
        String verificationCode
) {
}
