package com.hanserwei.hannote.oss.api;

import com.hanserwei.framework.response.Response;
import com.hanserwei.hannote.user.biz.model.vo.UpdatePasswordReqVO;
import com.hanserwei.hannote.user.biz.model.vo.UpdateUserInfoReqVO;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.service.annotation.PostExchange;


/**
 * @author hanserwei
 */
public interface FileClient {

    String PREFIX = "/user";


    /**
     * 修改用户密码
     * @param updatePasswordReqVO 用户修改密码实体类
     * @return 响应结果
     */
    @PostExchange(url = PREFIX + "/password/update", contentType = MediaType.APPLICATION_JSON_VALUE)
    Response<?> updatePassword(@Validated @RequestBody UpdatePasswordReqVO updatePasswordReqVO);

    /**
     * 用户详细信息修改
     * @param updateUserInfoReqVO 用户信息修改实体类
     * @return 响应结果
     */
    @PostExchange(url = PREFIX + "/update", contentType = MediaType.MULTIPART_FORM_DATA_VALUE)
    Response<?> updateUserInfo(@Validated UpdateUserInfoReqVO updateUserInfoReqVO);
}
