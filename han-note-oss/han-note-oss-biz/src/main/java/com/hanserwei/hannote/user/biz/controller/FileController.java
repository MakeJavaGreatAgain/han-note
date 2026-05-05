package com.hanserwei.hannote.user.biz.controller;

import com.hanserwei.framework.response.Response;
import com.hanserwei.hannote.user.api.FileClient;
import com.hanserwei.hannote.user.biz.service.FileService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author hanserwei
 */
@RestController
@Slf4j
public class FileController implements FileClient {

    @Resource
    private FileService fileService;

    @Override
    public Response<?> uploadFile(@RequestPart(value = "file") MultipartFile file) {
        return fileService.uploadFile(file);
    }

}
