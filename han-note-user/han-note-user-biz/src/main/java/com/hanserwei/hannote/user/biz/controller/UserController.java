package com.hanserwei.hannote.user.biz.controller;

import com.hanserwei.framework.biz.operationlog.aspect.ApiOperationLog;
import com.hanserwei.framework.response.Response;
import com.hanserwei.hannote.user.biz.domain.dataobject.UserDO;
import com.hanserwei.hannote.user.biz.domain.dataobject.UserRoleRelDO;
import com.hanserwei.hannote.user.biz.model.vo.UpdatePasswordReqVO;
import com.hanserwei.hannote.user.biz.model.vo.UpdateUserInfoReqVO;
import com.hanserwei.hannote.user.biz.service.UserService;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author hanserwei
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Resource
    private UserService userService;
    /**
     * 用户信息修改
     *
     * @param updateUserInfoReqVO 更新用户信息请求
     * @return 更新结果
     */
    @PostMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Response<?> updateUserInfo(@Validated UpdateUserInfoReqVO updateUserInfoReqVO) {
        return userService.updateUserInfo(updateUserInfoReqVO);
    }

    /**
     * 用户密码修改
     * @param updatePasswordReqVO 修改用户密码
     * @return 更新结果
     */
    @PostMapping("/password/update")
    @ApiOperationLog(description = "修改密码")
    public Response<?> updatePassword(@Validated @RequestBody UpdatePasswordReqVO updatePasswordReqVO) {
        return userService.updatePassword(updatePasswordReqVO);
    }
    @GetMapping("/get-by-phone")
    @ApiOperationLog(description = "通过手机话查找用户")
    public Response<?> selectByPhone(@RequestParam("phone") @NotBlank String phone){
        return userService.selectByPhone(phone);
    }
    @PostMapping("/insert")
    @ApiOperationLog(description = "用户信息插入")
    public Response<?> insertUser(@RequestBody UserDO userDO){
        return userService.insertUser(userDO);
    }
    @PostMapping("/userRoleRel/insert")
    @ApiOperationLog(description = "用户角色关系插入")
    public Response<?> insertUserRoleRel(@RequestBody UserRoleRelDO userRoleDO){
        return userService.insertUserRoleRel(userRoleDO);
    }
    @GetMapping("/userRoleRel/select-by-userId")
    @ApiOperationLog(description = "通过用户Id查询用户角色")
    public Response<?> selectUserRoleRelByUserId(@RequestParam("userId") Long userId){
        return userService.selectUserRoleRelByUserId(userId);
    }
    @GetMapping("/role/select-by-primaryKey")
    @ApiOperationLog(description = "用户权限查询")
    public Response<?> selectByPrimaryKey(@RequestParam("primaryKey") Long primaryKey){
        return userService.selectByPrimaryKey(primaryKey);
    }
    @GetMapping("/role/select-roleKeyList-By-IdList")
    @ApiOperationLog(description = "通过用户角色Id列表查询角色列表")
    public Response<?> selectRoleKeyListByIdList(@RequestParam("roleIdList") List<Long> roleIdList){
        return userService.selectRoleKeyListByIdList(roleIdList);
    }


}