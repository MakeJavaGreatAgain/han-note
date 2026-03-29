package com.hanserwei.hannote.oss.biz.service.impl;

import com.hanserwei.framework.response.Response;
import com.hanserwei.hannote.oss.biz.service.FileService;
import com.hanserwei.hannote.oss.biz.strategy.FileStrategy;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author hanserwei
 */
@Service
public class FileServiceImpl implements FileService {

    @Resource
    private FileStrategy fileStrategy;

    /**
     * 上传文件
     *
     * @param file 文件
     * @return 响应结果
     */
    @Override
    public Response<?> uploadFile(MultipartFile file) {
        String url = fileStrategy.uploadFile(file, "han-note");
        return Response.success(url);
    }
}
