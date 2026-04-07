package com.hanserwei.hannote.user.biz.config.rustfs;

import jakarta.annotation.Resource;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.net.URI;

/**
 * Rustfs 对象存储自动配置类
 * <p>
 * 负责创建和配置 S3Client Bean，用于与 Rustfs 服务进行通信。
 * Rustfs 兼容 S3 协议，因此使用 AWS S3 SDK 作为客户端。
 * </p>
 * <p>
 * 关键配置说明：
 * <ul>
 *   <li>endpointOverride: 指定 Rustfs 服务地址</li>
 *   <li>forcePathStyle: 必须设置为 true，Rustfs 使用路径风格 URL</li>
 *   <li>region: Rustfs 不校验区域，可设置为任意值</li>
 * </ul>
 * </p>
 *
 * @author hanserwei
 */
@Configuration
@EnableConfigurationProperties(RustfsProperties.class)
public class RustfsConfig {

    @Resource
    private RustfsProperties rustfsProperties;

    /**
     * 创建 S3Client Bean
     * <p>
     * 配置说明：
     * <ul>
     *   <li>endpointOverride: 设置 Rustfs 服务端点地址</li>
     *   <li>region: 设置区域信息，Rustfs 不校验，使用配置值即可</li>
     *   <li>credentialsProvider: 配置访问凭证</li>
     *   <li>forcePathStyle: 启用路径风格访问，Rustfs 必须开启</li>
     * </ul>
     * </p>
     *
     * @return 配置好的 S3Client 实例
     */
    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .endpointOverride(URI.create(rustfsProperties.endpoint()))
                .region(Region.of(rustfsProperties.region()))
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(rustfsProperties.accessKey(), rustfsProperties.secretKey())
                        )
                )
                .forcePathStyle(true)
                .build();
    }
}
