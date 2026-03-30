package com.hanserwei.hannote.oss.biz.strategy.impl;

import com.hanserwei.hannote.oss.biz.config.aliyun.AliyunOssClient;
import com.hanserwei.hannote.oss.biz.strategy.FileStrategy;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author hanserwei
 */
@Slf4j
public class AliyunOSSFileStrategy implements FileStrategy {

    @Resource
    private AliyunOssClient aliyunOssClient;

    /**
     * 文件上传
     *
     * @param file       文件
     * @param bucketName 桶名称
     * @return 文件上传后的地址
     */
    @Override
    public String uploadFile(MultipartFile file, String bucketName) {
        log.info(">>>> 文件上传开始至阿里云 OSS!,bucketName: {}", bucketName);
        String url = aliyunOssClient.upload(file, bucketName);
        log.info(">>>> 文件上传完成至阿里云 OSS! {}", url);
        return url;
    }

    /**
     * 文件上传到默认存储桶
     *
     * @param file 文件
     * @return 文件上传后的地址
     */
    @Override
    public String uploadFile(MultipartFile file) {
        log.info(">>>> 文件上传开始至阿里云 OSS!");
        return aliyunOssClient.upload(file);
    }
}