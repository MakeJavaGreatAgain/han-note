package com.hanserwei.hannote.user.biz.service;

import com.hanserwei.framework.response.Response;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author hanserwei
 */
public interface FileService {

    /**
     * 上传文件
     *
     * @param file 文件
     * @return 响应结果
     */
    Response<?> uploadFile(MultipartFile file);
}