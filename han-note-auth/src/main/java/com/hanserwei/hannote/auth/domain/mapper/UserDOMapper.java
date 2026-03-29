package com.hanserwei.hannote.auth.domain.mapper;

import com.hanserwei.hannote.auth.domain.dataobject.UserDO;

/**
 * @author hanserwei
 */
public interface UserDOMapper {

    /**
     * 根据手机号查询记录
     *
     * @param phone 手机号
     * @return UserDO
     */
    UserDO selectByPhone(String phone);

    /**
     * 插入新的用户记录到数据库
     *
     * @param userDO 用户数据对象,包含要插入的用户信息,
     *               如ID、手机号、昵称、密码、状态等
     */
    void insert(UserDO userDO);

    /**
     * 根据主键更新用户信息
     *
     * @param userDO 用户数据对象,包含要更新的用户信息,
     *               如手机号、昵称、密码、状态等
     */
    void updateByPrimaryKeySelective(UserDO userDO);
}
