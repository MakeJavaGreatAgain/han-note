package com.hanserwei.hannote.user.biz.service;

import com.hanserwei.framework.response.Response;
import com.hanserwei.hannote.user.biz.model.vo.UpdatePasswordReqVO;
import com.hanserwei.hannote.user.biz.model.vo.UpdateUserInfoReqVO;

/**
 * @author hanserwei
 */
public interface UserService {

    /**
     * 更新用户信息
     *
     * @param updateUserInfoReqVO 更新用户信息请求
     * @return 更新结果
     */
    Response<?> updateUserInfo(UpdateUserInfoReqVO updateUserInfoReqVO);
    /**
     * 修改密码
     *
     * @param updatePasswordReqVO 修改密码请求
     * @return 修改密码结果
     */
    Response<?> updatePassword(UpdatePasswordReqVO updatePasswordReqVO);
}
