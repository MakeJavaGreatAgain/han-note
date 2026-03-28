package com.hanserwei.hannote.auth.alarm;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author hanserwei
 */
@Slf4j
@Configuration
public class AlarmConfig {


    @Bean
    @RefreshScope
    public AlarmInterface alarmHelper(@Value("${alarm.type}") String alarmType) {
        log.info(">>>> 正在重建 Alarm Bean, 当前类型为: {}", alarmType);
        // 根据配置文件中的告警类型，初始化选择不同的告警实现类
        return switch (alarmType) {
            case "sms" -> new SmsAlarmHelper();
            case "mail" -> new MailAlarmHelper();
            default -> throw new IllegalArgumentException("错误的告警类型...");
        };
    }
}