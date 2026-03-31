package com.hanserwei.hannote.user.biz.domain.mapper;

import com.hanserwei.hannote.user.biz.domain.dataobject.UserDO;

/**
 * @author hanserwei
 */
public interface UserDOMapper {

    /**
     * 按主键选择性更新用户信息
     *
     * @param userDO 用户信息
     */
    void updateByPrimaryKeySelective(UserDO userDO);
}
