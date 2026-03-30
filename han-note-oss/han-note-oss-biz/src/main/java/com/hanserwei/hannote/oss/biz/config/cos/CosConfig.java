package com.hanserwei.hannote.oss.biz.config.cos;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.region.Region;
import jakarta.annotation.Resource;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 腾讯云 COS 对象存储自动配置类
 * <p>
 * 负责创建和配置 COSClient Bean，用于与腾讯云 COS 服务进行通信。
 * </p>
 * <p>
 * 配置说明：
 * <ul>
 *   <li>secretId: 腾讯云 API 密钥 SecretId</li>
 *   <li>secretKey: 腾讯云 API 密钥 SecretKey</li>
 *   <li>region: Bucket 所在地域，如 ap-guangzhou</li>
 * </ul>
 * </p>
 *
 * @author hanserwei
 */
@Configuration
@EnableConfigurationProperties(CosProperties.class)
public class CosConfig {

    @Resource
    private CosProperties cosProperties;

    /**
     * 创建腾讯云 COSClient Bean
     * <p>
     * COSClient 是线程安全的，建议在程序生命周期内只创建一个实例。
     * 使用完毕后需要调用 shutdown() 方法关闭。
     * </p>
     *
     * @return 配置好的 COSClient 实例
     */
    @Bean
    public COSClient tencentCosClient() {
        COSCredentials cred = new BasicCOSCredentials(
                cosProperties.secretId(),
                cosProperties.secretKey()
        );

        Region region = new Region(cosProperties.region());
        ClientConfig clientConfig = new ClientConfig(region);

        clientConfig.setHttpProtocol(HttpProtocol.https);


        return new COSClient(cred, clientConfig);
    }
}
