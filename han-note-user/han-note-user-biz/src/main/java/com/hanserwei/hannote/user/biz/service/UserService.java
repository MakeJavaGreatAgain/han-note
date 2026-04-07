package com.hanserwei.hannote.user.biz.service;

import com.hanserwei.framework.response.Response;
import com.hanserwei.hannote.user.biz.domain.dataobject.UserDO;
import com.hanserwei.hannote.user.biz.domain.dataobject.UserRoleRelDO;
import com.hanserwei.hannote.user.biz.model.vo.UpdatePasswordReqVO;
import com.hanserwei.hannote.user.biz.model.vo.UpdateUserInfoReqVO;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

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

    /**
     * 通过手机号查找用户
     * @param phone 用户手机号
     * @return 用户实体类
     */
    Response<?> selectByPhone(String phone);

    /**
     * 插入用户实体类
     * @param userDO 用户实体类
     * @return 返回空
     */
    Response<?> insertUser(UserDO userDO);

    /**
     * 插入用户角色关系实体
     * @param userRoleDO 用户角色实体类
     * @return 返回为空
     */
    Response<?> insertUserRoleRel(UserRoleRelDO userRoleDO);

    /**
     *  根据用户Id查用户角色关系
     * @param userId 用户Id
     * @return 用户角色关系列表
     */
    Response<?> selectUserRoleRelByUserId(Long userId);

    /**
     * 通过主键查角色实体类
     * @param primaryKey 主键
     * @return 角色实体类
     */
    Response<?> selectByPrimaryKey(Long primaryKey);

    /**
     * 通过用户角色Id列表查用户角色key列表
     * @param roleIdList 角色key列表
     * @return 角色key列表
     */
    Response<?> selectRoleKeyListByIdList(List<Long> roleIdList);
}
