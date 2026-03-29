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
import java.util.function.Function;

/**
 * 验证码业务实现类
 *
 * @author hanserwei
 */
@Slf4j
@Service
public class VerificationCodeServiceImpl implements VerificationCodeService {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource(name = "authLoginExecutor")
    private Executor authLoginExecutor;

    @Resource(name = "authUpdatePasswordExecutor")
    private Executor authUpdatePasswordExecutor;

    @Resource
    private AliyunSmsHelper aliyunSmsHelper;

    // --- 常量提取 ---
    /**
     * 短信签名
     */
    private static final String SMS_SIGN_NAME = "速通互联验证码";
    /**
     * 短信模板码
     */
    private static final String SMS_TEMPLATE_CODE = "100001";
    /**
     * 验证码长度
     */
    private static final int CODE_LENGTH = 6;
    /**
     * 验证码有效期（分钟）
     */
    private static final long EXPIRE_TIME = 3L;

    @Override
    public Response<?> sendLogin(SendVerificationCodeReqVO reqVO) {
        return sendVerificationCode(reqVO.phone(), RedisKeyConstants::buildVerificationCodeKey, authLoginExecutor);
    }

    @Override
    public Response<?> sendUpdatePassword(SendVerificationCodeReqVO reqVO) {
        return sendVerificationCode(reqVO.phone(), RedisKeyConstants::buildUpdatePasswordVerificationCodeKey, authUpdatePasswordExecutor);
    }

    /**
     * 通用发送验证码逻辑提取
     *
     * @param phone        手机号
     * @param keyGenerator Redis Key 生成函数
     * @param executor     执行发送任务的线程池
     * @return 响应结果
     */
    private Response<?> sendVerificationCode(String phone, Function<String, String> keyGenerator, Executor executor) {
        // 1. 构建并校验 Key
        String redisKey = keyGenerator.apply(phone);

        // 检查是否发送频繁（此处可优化为 setIfAbsent 原子操作防止并发漏洞）
        if (Boolean.TRUE.equals(redisTemplate.hasKey(redisKey))) {
            throw new BizException(ResponseCodeEnum.VERIFICATION_CODE_SEND_FREQUENTLY);
        }

        // 2. 生成 6 位验证码
        String code = RandomUtil.randomNumbers(CODE_LENGTH);

        // 3. 异步发送短信
        executor.execute(() -> {
            try {
                // 构建 JSON 参数：{"code":"123456","min":"3"}
                String templateParam = String.format("{\"code\":\"%s\",\"min\":\"%d\"}", code, EXPIRE_TIME);
                aliyunSmsHelper.sendMessage(SMS_SIGN_NAME, SMS_TEMPLATE_CODE, phone, templateParam);
                log.info("==> 验证码短信发送成功, 手机号: {}, 验证码: {}", phone, code);
            } catch (Exception e) {
                log.error("==> 验证码短信发送失败, 手机号: {}", phone, e);
            }
        });

        // 4. 存入 Redis 并设置过期时间
        redisTemplate.opsForValue().set(redisKey, code, EXPIRE_TIME, TimeUnit.MINUTES);

        log.info("==> 验证码已存入缓存, 手机号: {}, Key: {}", phone, redisKey);
        return Response.success();
    }
}
