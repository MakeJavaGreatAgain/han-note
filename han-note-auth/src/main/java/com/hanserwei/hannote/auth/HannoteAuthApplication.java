package com.hanserwei.hannote.auth;

import com.hanserwei.hannote.auth.sms.AliyunAccessKeyProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @author hanserwei
 */
@SpringBootApplication
@MapperScan("com.hanserwei.hannote.auth.domain.mapper")
@EnableConfigurationProperties({AliyunAccessKeyProperties.class})
public class HannoteAuthApplication {
    static void main(String[] args) {
        SpringApplication.run(HannoteAuthApplication.class, args);
    }
}
