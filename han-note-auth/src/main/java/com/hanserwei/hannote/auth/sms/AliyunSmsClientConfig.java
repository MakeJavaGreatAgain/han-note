package com.hanserwei.hannote.auth.sms;

import com.aliyun.auth.credentials.Credential;
import com.aliyun.auth.credentials.provider.StaticCredentialProvider;
import com.aliyun.sdk.service.dypnsapi20170525.AsyncClient;
import darabonba.core.client.ClientOverrideConfiguration;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author hanserwei
 */
@Configuration
public class AliyunSmsClientConfig {

    @Resource
    private AliyunAccessKeyProperties aliyunAccessKeyProperties;

    @Bean
    public AsyncClient smsAsyncClient() {
        Credential credential = Credential.builder()
                .accessKeyId(aliyunAccessKeyProperties.accessKeyId())
                .accessKeySecret(aliyunAccessKeyProperties.accessKeySecret())
                .build();

        StaticCredentialProvider provider = StaticCredentialProvider.create(credential);

        return AsyncClient.builder()
                .region(aliyunAccessKeyProperties.region())
                .credentialsProvider(provider)
                .overrideConfiguration(
                        ClientOverrideConfiguration.create()
                                .setEndpointOverride("dypnsapi.aliyuncs.com")
                )
                .build();
    }
}