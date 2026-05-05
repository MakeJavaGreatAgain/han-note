package com.hanserwei.hannote.user.api;

import com.hanserwei.framework.response.Response;
import com.hanserwei.hannote.user.biz.domain.dataobject.UserDO;
import com.hanserwei.hannote.user.biz.domain.dataobject.UserRoleRelDO;
import com.hanserwei.hannote.user.biz.model.vo.UpdatePasswordReqVO;
import com.hanserwei.hannote.user.biz.model.vo.UpdateUserInfoReqVO;

import org.springframework.validation.annotation.Validated;
import org.springframework.http.MediaType;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;

import java.util.List;

/**
 * @author hanserwei
 */
public interface UserClient {

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

    /**
     * 通过手机号查询用户
     * @param phone 手机号
     * @return 用户实体类
     */
    @GetExchange(url = PREFIX + "/get-by-phone")
    Response<?> selectByPhone(@RequestParam("phone")String phone);

    /**
     * 插入用户实体类
     *
     * @param userDO 用户实体类
     */
    @PostExchange(url = PREFIX + "/insert")
    void insertUser(@RequestBody UserDO userDO);

    /**
     * 插入用户角色关系实体
     *
     * @param userRoleDO 用户角色实体类
     */
    @PostExchange(url = PREFIX + "/userRoleRel/insert")
    void insertUserRoleRel(@RequestBody UserRoleRelDO userRoleDO);

    /**
     *  根据用户Id查用户角色关系
     * @param userId 用户Id
     * @return 用户角色关系列表
     */
    @GetExchange(url = PREFIX + "/userRoleRel/select-by-userId")
    Response<?> selectUserRoleRelByUserId(@RequestParam("userId") Long userId);
    /**
     * 通过主键查角色实体类
     * @param primaryKey 主键
     * @return 角色实体类
     */
    @GetExchange(url = PREFIX + "/role/select-by-primaryKey")
    Response<?> selectByPrimaryKey(@RequestParam("primaryKey") Long primaryKey);

    /**
     * 通过用户角色Id列表查用户角色key列表
     * @param roleIdList 角色key列表
     * @return 角色key列表
     */
    @GetExchange(url = PREFIX + "/role/select-roleKeyList-By-IdList")
    Response<?> selectRoleKeyListByIdList(@RequestParam("roleIdList") List<Long> roleIdList);

}
