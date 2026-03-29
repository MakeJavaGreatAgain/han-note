package com.hanserwei.hannote.auth.service;

import com.hanserwei.framework.response.Response;
import com.hanserwei.hannote.auth.model.vo.veriticationcode.SendVerificationCodeReqVO;

/**
 * @author hanserwei
 */
public interface VerificationCodeService {

    /**
     * 发送登录验证码
     *
     * @param sendVerificationCodeReqVO 验证码请求
     * @return 验证码发送结果
     */
    Response<?> sendLogin(SendVerificationCodeReqVO sendVerificationCodeReqVO);

    /**
     * 发送修改密码验证码
     *
     * @param sendVerificationCodeReqVO 验证码请求
     * @return 验证码发送结果
     */
    Response<?> sendUpdatePassword(SendVerificationCodeReqVO sendVerificationCodeReqVO);
}