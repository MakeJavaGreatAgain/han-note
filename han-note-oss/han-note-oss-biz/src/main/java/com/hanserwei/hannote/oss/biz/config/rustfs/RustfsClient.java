package com.hanserwei.hannote.oss.biz.config.rustfs;

import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.IdUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;

/**
 * Rustfs 对象存储客户端
 * <p>
 * 封装了基于 AWS S3 SDK 的文件上传操作，对外提供简洁的文件上传接口。
 * 上传成功后返回文件的完整访问 URL，可直接返回给前端使用。
 * </p>
 *
 * @author hanserwei
 */
@Slf4j
@Component
public class RustfsClient {

    @Resource
    private S3Client s3Client;

    @Resource
    private RustfsProperties rustfsProperties;

    /**
     * 上传文件到默认存储桶
     * <p>
     * 使用配置文件中配置的默认 bucket，文件名自动生成 UUID 避免重复。
     * </p>
     *
     * @param file 要上传的文件
     * @return 文件访问 URL
     */
    public String upload(MultipartFile file) {
        return upload(file, rustfsProperties.bucket());
    }

    /**
     * 上传文件到指定存储桶
     * <p>
     * 文件名自动生成 UUID 避免重复，保留原始文件扩展名。
     * </p>
     *
     * @param file       要上传的文件
     * @param bucketName 存储桶名称
     * @return 文件访问 URL
     */
    public String upload(MultipartFile file, String bucketName) {
        return upload(file, bucketName, generateKey(file.getOriginalFilename()));
    }

    /**
     * 上传文件到指定存储桶并指定文件键名
     * <p>
     * 最底层的上传方法，允许完全自定义存储桶和文件键名。
     * </p>
     *
     * @param file       要上传的文件
     * @param bucketName 存储桶名称
     * @param key        文件在存储桶中的键名（路径/文件名）
     * @return 文件访问 URL
     * @throws RuntimeException 当文件上传失败时抛出
     */
    public String upload(MultipartFile file, String bucketName, String key) {
        try {
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(request, RequestBody.fromBytes(file.getBytes()));
            log.info("文件上传成功，bucket={}，key={}", bucketName, key);

            return buildFileUrl(bucketName, key);
        } catch (IOException e) {
            log.error("文件上传失败：{}", e.getMessage(), e);
            throw new RuntimeException("文件上传失败", e);
        }
    }

    /**
     * 生成唯一的文件键名
     * <p>
     * 使用 UUID 生成唯一标识，并保留原始文件的扩展名。
     * </p>
     *
     * @param originalFilename 原始文件名
     * @return 生成的唯一文件键名
     */
    private String generateKey(String originalFilename) {
        String extension = FileNameUtil.extName(originalFilename);
        return IdUtil.simpleUUID() + (extension.isEmpty() ? "" : "." + extension);
    }

    /**
     * 构建文件访问 URL
     *
     * @param bucketName 存储桶名称
     * @param key        文件键名
     * @return 完整的文件访问 URL
     */
    private String buildFileUrl(String bucketName, String key) {
        return String.format("%s/%s/%s", rustfsProperties.endpoint(), bucketName, key);
    }
}
