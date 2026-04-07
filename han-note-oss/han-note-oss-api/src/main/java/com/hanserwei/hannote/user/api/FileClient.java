package com.hanserwei.hannote.user.api;

import com.hanserwei.framework.response.Response;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.service.annotation.PostExchange;

/**
 * @author hanserwei
 */
public interface FileClient {

    String PREFIX = "/file";

    /**
     * 上传文件
     *
     * @param file 文件
     * @return 响应结果
     */
    @PostExchange(url = PREFIX + "/upload", contentType = MediaType.MULTIPART_FORM_DATA_VALUE)
    Response<?> uploadFile(@RequestPart("file") MultipartFile file);
}
