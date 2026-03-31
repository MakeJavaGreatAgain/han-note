package com.hanserwei.hannote.user.biz.service;

import com.hanserwei.framework.response.Response;
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
}
