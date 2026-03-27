package com.hanserwei.hannote.auth.sms;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author hanserwei
 */
@ConfigurationProperties(prefix = "aliyun")
public record AliyunAccessKeyProperties(
        String accessKeyId,
        String accessKeySecret,
        String region
) {
}