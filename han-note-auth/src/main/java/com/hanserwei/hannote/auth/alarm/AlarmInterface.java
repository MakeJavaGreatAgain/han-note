package com.hanserwei.hannote.auth.alarm;

/**
 * @author hanserwei
 */
public interface AlarmInterface {

    /**
     * 发送告警信息
     *
     * @param message 警告信息
     * @return 是否发送成功
     */
    boolean send(String message);
}