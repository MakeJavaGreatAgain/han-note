package com.hanserwei.hannote.user.biz.factory;

import com.hanserwei.hannote.user.biz.strategy.FileStrategy;
import com.hanserwei.hannote.user.biz.strategy.impl.AliyunOSSFileStrategy;
import com.hanserwei.hannote.user.biz.strategy.impl.CosFileStrategy;
import com.hanserwei.hannote.user.biz.strategy.impl.RustfsFileStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author hanserwei
 */
@Configuration
public class FileStrategyFactory {

    /**
     * 存储策略类型，rustfs、aliyun、cos
     */
    public static final String STRATEGY_TYPE_RUSTFS = "rustfs";
    public static final String STRATEGY_TYPE_ALIYUN = "aliyun";
    public static final String STRATEGY_TYPE_COS = "cos";

    @Bean
    @RefreshScope
    public FileStrategy getFileStrategy(@Value("${storage.type}") String strategyType) {
        return switch (strategyType) {
            case STRATEGY_TYPE_RUSTFS -> new RustfsFileStrategy();
            case STRATEGY_TYPE_ALIYUN -> new AliyunOSSFileStrategy();
            case STRATEGY_TYPE_COS -> new CosFileStrategy();
            default -> throw new IllegalArgumentException("不可用的存储类型");
        };

    }

}