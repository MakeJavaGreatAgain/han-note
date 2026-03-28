package com.hanserwei.hannote.auth.domain.mapper;

import com.hanserwei.hannote.auth.domain.dataobject.UserRoleRelDO;

/**
 * @author hanserwei
 */
public interface UserRoleRelDOMapper {

    /**
     * 向数据库插入用户-角色关系记录
     *
     * @param userRoleDO 用户-角色关系数据对象,包含要插入的详细信息,
     *                   包括用户ID、角色ID、创建时间、更新时间以及逻辑删除状态
     */
    void insert(UserRoleRelDO userRoleDO);
}