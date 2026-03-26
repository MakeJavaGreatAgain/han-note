package com.hanserwei.framework.exception;

/**
 * @author hanserwei
 */
public interface BaseExceptionInterface {
    /**
     * 获取异常码
     *
     * @return 异常码
     */
    String getErrorCode();

    /**
     * 获取异常码信息
     *
     * @return 异常码信息
     */
    String getErrorMessage();
}
