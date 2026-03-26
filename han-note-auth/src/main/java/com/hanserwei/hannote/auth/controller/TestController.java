package com.hanserwei.hannote.auth.controller;

import com.hanserwei.framework.biz.operationlog.aspect.ApiOperationLog;
import com.hanserwei.framework.response.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * @author hanserwei
 */
@RestController
public class TestController {

    @GetMapping("/test")
    @ApiOperationLog(description = "测试接口")
    public Response<User> test() {
        return Response.success(new User("hanserwei", 20, LocalDateTime.now()));
    }

    record User(String name, Integer age, LocalDateTime createTime) {
    }
}