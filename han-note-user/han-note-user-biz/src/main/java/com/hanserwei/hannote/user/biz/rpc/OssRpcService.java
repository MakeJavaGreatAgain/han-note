package com.hanserwei.hannote.user.biz.rpc;

import java.util.Objects;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.hanserwei.framework.response.Response;
import com.hanserwei.hannote.oss.api.FileClient;
import jakarta.annotation.Resource;

/**
 * @author hanserwei
 */
@Component
@Slf4j
public class OssRpcService {

    @Resource
    private FileClient fileClient;

    public String uploadFile(MultipartFile file) {
        try {
            Response<?> response = fileClient.uploadFile(file);
            if (Objects.isNull(response) || !response.isSuccess() || Objects.isNull(response.getData())) {
                log.warn("upload file to oss fail, fileName: {}, response: {}", file.getOriginalFilename(), response);
                return null;
            }

            if (!(response.getData() instanceof String fileUrl) || StringUtils.isBlank(fileUrl)) {
                log.warn("upload file to oss fail, fileName: {}, responseData: {}", file.getOriginalFilename(),
                        response.getData());
                return null;
            }

            return fileUrl;
        }
        catch (Exception e) {
            log.error("upload file to oss error, fileName: {}", file.getOriginalFilename(), e);
            return null;
        }
    }
}
