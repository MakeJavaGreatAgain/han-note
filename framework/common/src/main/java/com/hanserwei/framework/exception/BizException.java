package com.hanserwei.framework.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * @author hanserwei
 */
@Getter
@Setter
public class BizException extends RuntimeException {
    /**
     * 异常吗
     */
    private String errorCode;
    /**
     * 错误信息
     */
    private String errorMessage;

    public BizException(BaseExceptionInterface baseExceptionInterface) {
        this.errorCode = baseExceptionInterface.getErrorCode();
        this.errorMessage = baseExceptionInterface.getErrorMessage();
    }
}
