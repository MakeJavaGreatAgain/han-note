package com.hanserwei.hannote.auth.service;

import com.hanserwei.framework.response.Response;
import com.hanserwei.hannote.auth.model.vo.veriticationcode.SendVerificationCodeReqVO;

/**
 * @author hanserwei
 */
public interface VerificationCodeService {

    /**
     * 发送短信验证码
     *
     * @param sendVerificationCodeReqVO 严重码请求
     * @return 验证码发送结果
     */
    Response<?> send(SendVerificationCodeReqVO sendVerificationCodeReqVO);
}