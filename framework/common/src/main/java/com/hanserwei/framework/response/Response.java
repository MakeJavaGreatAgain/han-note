package com.hanserwei.framework.response;

import com.hanserwei.framework.exception.BaseExceptionInterface;
import com.hanserwei.framework.exception.BizException;
import lombok.Data;


import java.io.Serializable;

/**
 * 统一响应结果类
 *
 * @author hanserwei
 */
@Data
public class Response<T> implements Serializable {

    /**
     * 是否成功,默认为true
     */
    private boolean success = true;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 异常码
     */
    private String errorCode;

    /**
     * 响应数据
     */
    private T data;

    /**
     * 构建成功响应(无数据)
     *
     * @param <T> 响应数据类型
     * @return 成功响应对象
     */
    public static <T> Response<T> success() {
        return new Response<>();
    }

    /**
     * 构建成功响应(带数据)
     *
     * @param data 响应数据
     * @param <T>  响应数据类型
     * @return 成功响应对象
     */
    public static <T> Response<T> success(T data) {
        Response<T> response = new Response<>();
        response.setData(data);
        return response;
    }

    /**
     * 构建失败响应(无异常信息)
     *
     * @param <T> 响应数据类型
     * @return 失败响应对象
     */
    public static <T> Response<T> fail() {
        Response<T> response = new Response<>();
        response.setSuccess(false);
        return response;
    }

    /**
     * 构建失败响应(带错误消息)
     *
     * @param errorMessage 错误消息
     * @param <T>          响应数据类型
     * @return 失败响应对象
     */
    public static <T> Response<T> fail(String errorMessage) {
        Response<T> response = new Response<>();
        response.setSuccess(false);
        response.setMessage(errorMessage);
        return response;
    }

    /**
     * 构建失败响应(带错误码和错误消息)
     *
     * @param errorCode    错误码
     * @param errorMessage 错误消息
     * @param <T>          响应数据类型
     * @return 失败响应对象
     */
    public static <T> Response<T> fail(String errorCode, String errorMessage) {
        Response<T> response = new Response<>();
        response.setSuccess(false);
        response.setErrorCode(errorCode);
        response.setMessage(errorMessage);
        return response;
    }

    /**
     * 构建失败响应(基于业务异常)
     *
     * @param bizException 业务异常对象
     * @param <T>          响应数据类型
     * @return 失败响应对象
     */
    public static <T> Response<T> fail(BizException bizException) {
        Response<T> response = new Response<>();
        response.setSuccess(false);
        response.setErrorCode(bizException.getErrorCode());
        response.setMessage(bizException.getErrorMessage());
        return response;
    }

    /**
     * 构建失败响应(基于异常接口)
     *
     * @param baseExceptionInterface 异常接口对象
     * @param <T>                    响应数据类型
     * @return 失败响应对象
     */
    public static <T> Response<T> fail(BaseExceptionInterface baseExceptionInterface) {
        Response<T> response = new Response<>();
        response.setSuccess(false);
        response.setErrorCode(baseExceptionInterface.getErrorCode());
        response.setMessage(baseExceptionInterface.getErrorMessage());
        return response;
    }

}