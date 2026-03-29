package com.hanserwei.hannote.oss.biz.config.aliyun;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 阿里云 OSS 对象存储配置属性
 * <p>
 * 用于从配置文件中读取阿里云 OSS 相关的连接配置信息，
 * 包括访问凭证、区域和默认存储桶等。
 * </p>
 *
 * @param endpoint   OSS 服务端点，例如：https://oss-cn-hangzhou.aliyuncs.com
 * @param accessKey  访问密钥（Access Key ID）
 * @param secretKey  秘密密钥（Secret Access Key）
 * @param region     地域，例如：cn-hangzhou
 * @param bucket     默认存储桶名称
 * @author hanserwei
 */
@ConfigurationProperties(prefix = "aliyun.oss")
public record AliyunOssProperties(
        String endpoint,
        String accessKey,
        String secretKey,
        String region,
        String bucket
) {
}
