package com.hanserwei.hannote.user.biz.strategy.impl;

import com.hanserwei.hannote.user.biz.config.cos.CosClient;
import com.hanserwei.hannote.user.biz.strategy.FileStrategy;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

/**
 * 腾讯云 COS 文件存储策略
 * <p>
 * 实现基于腾讯云 COS 的文件上传功能。
 * </p>
 *
 * @author hanserwei
 */
@Slf4j
public class CosFileStrategy implements FileStrategy {

    @Resource
    private CosClient cosClient;

    /**
     * 文件上传到指定存储桶
     *
     * @param file       文件
     * @param bucketName 存储桶名称
     * @return 文件上传后的访问地址
     */
    @Override
    public String uploadFile(MultipartFile file, String bucketName) {
        log.info(">>>> 文件上传开始至腾讯云 COS!, bucketName: {}", bucketName);
        String url = cosClient.upload(file, bucketName);
        log.info(">>>> 文件上传完成至腾讯云 COS! {}", url);
        return url;
    }

    /**
     * 文件上传到默认存储桶
     *
     * @param file 文件
     * @return 文件上传后的访问地址
     */
    @Override
    public String uploadFile(MultipartFile file) {
        log.info(">>>> 文件上传开始至腾讯云 COS!");
        return cosClient.upload(file);
    }
}
