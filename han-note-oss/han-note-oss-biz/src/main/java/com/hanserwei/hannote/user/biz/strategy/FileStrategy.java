package com.hanserwei.hannote.user.biz.strategy;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author hanserwei
 */
public interface FileStrategy {

    /**
     * 文件上传
     *
     * @param file       文件
     * @param bucketName 桶名称
     * @return 文件上传后的地址
     */
    String uploadFile(MultipartFile file, String bucketName);

    /**
     * 文件上传
     *
     * @param file 文件
     * @return 文件上传后的地址
     */
    String uploadFile(MultipartFile file);
}