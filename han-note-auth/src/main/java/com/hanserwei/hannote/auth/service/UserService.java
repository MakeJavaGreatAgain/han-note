package com.hanserwei.hannote.auth.service;

import com.hanserwei.framework.response.Response;
import com.hanserwei.hannote.auth.model.vo.user.UserLoginReqVO;

/**
 * @author hanserwei
 */
public interface UserService {

    /**
     * 登录与注册
     *
     * @param userLoginReqVO 用户登录请求
     * @return token令牌
     */
    Response<String> loginAndRegister(UserLoginReqVO userLoginReqVO);

    /**
     * 登出
     *
     * @return 登出结果
     */
    Response<?> logout();
}