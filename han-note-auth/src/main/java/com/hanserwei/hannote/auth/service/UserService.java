package com.hanserwei.hannote.auth.service;

import com.hanserwei.framework.response.Response;
import com.hanserwei.hannote.auth.model.vo.user.UpdatePasswordReqVO;
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
     * 修改密码
     *
     * @param updatePasswordReqVO 修改密码请求
     * @return 修改密码结果
     */
    Response<?> updatePassword(UpdatePasswordReqVO updatePasswordReqVO);

    /**
     * 登出
     *
     * @return 登出结果
     */
    Response<?> logout();
}