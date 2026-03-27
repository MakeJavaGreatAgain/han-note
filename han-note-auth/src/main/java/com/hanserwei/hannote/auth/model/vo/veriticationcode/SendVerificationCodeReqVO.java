package com.hanserwei.hannote.auth.model.vo.veriticationcode;

import jakarta.validation.constraints.NotBlank;

/**
 * @author hanserwei
 */
public record SendVerificationCodeReqVO(
        @NotBlank(message = "手机号不能为空")
        String phone
) {
}