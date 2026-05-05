package com.hanserwei.hannote.user.biz.config.aliyun;

import com.aliyun.sdk.service.oss2.OSSClient;
import com.aliyun.sdk.service.oss2.credentials.StaticCredentialsProvider;
import jakarta.annotation.Resource;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 阿里云 OSS 对象存储自动配置类
 * <p>
 * 负责创建和配置 OSSClient Bean，用于与阿里云 OSS 服务进行通信。
 * 使用阿里云 OSS SDK V2 版本。
 * </p>
 * <p>
 * 配置说明：
 * <ul>
 *   <li>endpoint: OSS 服务端点地址</li>
 *   <li>region: Bucket 所在地域</li>
 *   <li>credentialsProvider: 访问凭证提供者</li>
 * </ul>
 * </p>
 *
 * @author hanserwei
 */
@Configuration
@EnableConfigurationProperties(AliyunOssProperties.class)
public class AliyunOssConfig {

    @Resource
    private AliyunOssProperties aliyunOssProperties;

    /**
     * 创建 OSSClient Bean
     * <p>
     * 配置说明：
     * <ul>
     *   <li>credentialsProvider: 使用静态凭证提供者，配置 AccessKey 和 SecretKey</li>
     *   <li>region: 设置 Bucket 所在地域</li>
     *   <li>endpoint: 设置 OSS 服务端点（可选）</li>
     * </ul>
     * </p>
     *
     * @return 配置好的 OSSClient 实例
     */
    @Bean
    public OSSClient ossClient() {
        StaticCredentialsProvider credentialsProvider = new StaticCredentialsProvider(
                aliyunOssProperties.accessKey(),
                aliyunOssProperties.secretKey()
        );

        return OSSClient.newBuilder()
                .credentialsProvider(credentialsProvider)
                .region(aliyunOssProperties.region())
                .endpoint(aliyunOssProperties.endpoint())
                .useCName(true)
                .build();
    }
}
