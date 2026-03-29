package com.hanserwei.hannote.oss.biz.strategy.impl;

import com.hanserwei.hannote.oss.biz.strategy.FileStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author hanserwei
 */
@Slf4j
public class CosFileStrategy implements FileStrategy {
    /**
     * 文件上传
     *
     * @param file       文件
     * @param bucketName 桶名称
     * @return 文件上传后的地址
     */
    @Override
    public String uploadFile(MultipartFile file, String bucketName) {
        log.info(">>>> 文件上传开始至腾讯Cos!");
        return "";
    }

    /**
     * 文件上传
     *
     * @param file 文件
     * @return 文件上传后的地址
     */
    @Override
    public String uploadFile(MultipartFile file) {
        return "";
    }
}
