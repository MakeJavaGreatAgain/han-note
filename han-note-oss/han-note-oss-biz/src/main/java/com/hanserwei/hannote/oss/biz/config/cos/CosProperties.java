package com.hanserwei.hannote.oss.biz.config.cos;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 腾讯云 COS 对象存储配置属性
 * <p>
 * 用于从配置文件中读取腾讯云 COS 相关的连接配置信息，
 * 包括访问凭证、区域和默认存储桶等。
 * </p>
 *
 * @param secretId      访问密钥（SecretId）
 * @param secretKey     秘密密钥（SecretKey）
 * @param region        地域，例如：ap-guangzhou
 * @param bucket        默认存储桶名称
 * @param customDomain  自定义访问域名，例如：https://note.likeyy.love
 * @author hanserwei
 */
@ConfigurationProperties(prefix = "tencent.cos")
public record CosProperties(
        String secretId,
        String secretKey,
        String region,
        String bucket,
        String customDomain
) {
}
