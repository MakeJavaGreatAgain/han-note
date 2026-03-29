package com.hanserwei.hannote.oss.biz.config.rustfs;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Rustfs 对象存储配置属性
 * <p>
 * 用于从配置文件中读取 Rustfs 相关的连接配置信息，
 * 包括服务端点、访问凭证、区域和默认存储桶等。
 * </p>
 *
 * @param endpoint  Rustfs 服务地址，例如：http://192.168.1.100:9000
 * @param accessKey 访问密钥（Access Key ID）
 * @param secretKey 秘密密钥（Secret Access Key）
 * @param region    区域，RustFS 通常不校验，可设置为 us-east-1
 * @param bucket    默认存储桶名称
 * @author hanserwei
 */
@ConfigurationProperties(prefix = "rustfs")
public record RustfsProperties(
        String endpoint,
        String accessKey,
        String secretKey,
        String region,
        String bucket
) {
}
