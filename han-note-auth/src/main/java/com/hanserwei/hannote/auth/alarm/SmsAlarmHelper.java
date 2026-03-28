package com.hanserwei.hannote.auth.alarm;

import lombok.extern.slf4j.Slf4j;

/**
 * @author hanserwei
 */
@Slf4j
public class SmsAlarmHelper implements AlarmInterface {
    /**
     * 发送告警信息
     *
     * @param message 警告信息
     * @return 是否发送成功
     */
    @Override
    public boolean send(String message) {
        log.info("==> 【短信告警】：{}", message);

        // 业务逻辑...

        return true;
    }
}
