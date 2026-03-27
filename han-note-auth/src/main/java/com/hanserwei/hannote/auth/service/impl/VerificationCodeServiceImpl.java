package com.hanserwei.hannote.auth.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.hanserwei.framework.exception.BizException;
import com.hanserwei.framework.response.Response;
import com.hanserwei.hannote.auth.constant.RedisKeyConstants;
import com.hanserwei.hannote.auth.enums.ResponseCodeEnum;
import com.hanserwei.hannote.auth.model.vo.veriticationcode.SendVerificationCodeReqVO;
import com.hanserwei.hannote.auth.service.VerificationCodeService;
import com.hanserwei.hannote.auth.sms.AliyunSmsHelper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

/**
 * @author hanserwei
 */
@Slf4j
@Service
public class VerificationCodeServiceImpl implements VerificationCodeService {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource(name = "taskExecutor")
    private Executor taskExecutor;

    @Resource
    private AliyunSmsHelper aliyunSmsHelper;

    @Override
    public Response<?> send(SendVerificationCodeReqVO sendVerificationCodeReqVO) {
        // 手机号
        String phone = sendVerificationCodeReqVO.phone();

        // 构造验证码Key
        String verificationCodeKey = RedisKeyConstants.buildVerificationCodeKey(phone);

        // 判断是否已经发送Key
        if (redisTemplate.hasKey(verificationCodeKey)) {
            throw new BizException(ResponseCodeEnum.VERIFICATION_CODE_SEND_FREQUENTLY);
        }

        // 生成6位验证码
        String verificationCode = RandomUtil.randomNumbers(6);

        // 调用第三方短信发送服务
        taskExecutor.execute(() -> {
            String signName = "速通互联验证码"; // 签名，个人测试签名无法修改
            String templateCode = "100001"; // 短信模板编码
            // 短信模板参数，code 表示要发送的验证码；min 表示验证码有时间时长，即 3 分钟
            String templateParam = String.format("{\"code\":\"%s\",\"min\":\"3\"}", verificationCode);
            aliyunSmsHelper.sendMessage(signName, templateCode, phone, templateParam);
        });

        log.info("==> 手机号: {}, 已发送验证码：【{}】", phone, verificationCode);

        // 存储验证码到 redis, 并设置过期时间为 3 分钟
        redisTemplate.opsForValue().set(verificationCodeKey, verificationCode, 3, TimeUnit.MINUTES);

        return Response.success();
    }
}
