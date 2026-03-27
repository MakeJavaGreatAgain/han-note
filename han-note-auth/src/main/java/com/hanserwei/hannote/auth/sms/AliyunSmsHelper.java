package com.hanserwei.hannote.auth.sms;

import com.aliyun.sdk.service.dypnsapi20170525.AsyncClient;
import com.aliyun.sdk.service.dypnsapi20170525.models.SendSmsVerifyCodeRequest;
import com.aliyun.sdk.service.dypnsapi20170525.models.SendSmsVerifyCodeResponse;
import com.hanserwei.framework.utils.JsonUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author hanserwei
 */
@Component
@Slf4j
public class AliyunSmsHelper {

    @Resource
    private AsyncClient smsAsyncClient;

    public void sendMessage(String signName, String templateCode, String phone, String templateParam) {
        SendSmsVerifyCodeRequest request = SendSmsVerifyCodeRequest.builder()
                .signName(signName)
                .templateCode(templateCode)
                .phoneNumber(phone)
                .templateParam(templateParam)
                .build();

        try {
            log.info("==> 开始短信发送, phone: {}, signName: {}, templateCode: {}, templateParam: {}", 
                    phone, signName, templateCode, templateParam);

            SendSmsVerifyCodeResponse response = smsAsyncClient.sendSmsVerifyCode(request).get();

            log.info("==> 短信发送成功, response: {}", JsonUtils.toJsonString(response));
        } catch (Exception error) {
            log.error("==> 短信发送错误: ", error);
        }
    }
}