package com.hanserwei.hannote.oss.biz.strategy.impl;

import com.hanserwei.hannote.oss.biz.config.rustfs.RustfsClient;
import com.hanserwei.hannote.oss.biz.strategy.FileStrategy;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author hanserwei
 */
@Slf4j
public class RustfsFileStrategy implements FileStrategy {

    @Resource
    private RustfsClient rustfsClient;

    /**
     * 文件上传
     *
     * @param file 文件
     * @return 文件上传后的地址
     */
    @Override
    public String uploadFile(MultipartFile file, String bucketName) {
        log.info(">>>> 文件上传开始至Rustfs!,bucketName: {}", bucketName);
        String url = rustfsClient.upload(file);
        log.info(">>>> 文件上传完成至Rustfs! {}", url);
        return url;
    }

    /**
     * 文件上传
     *
     * @param file 文件
     * @return 文件上传后的地址
     */
    @Override
    public String uploadFile(MultipartFile file) {
        log.info(">>>> 文件上传开始至Rustfs!,bucketName: han-note");
        return rustfsClient.upload(file);
    }
}