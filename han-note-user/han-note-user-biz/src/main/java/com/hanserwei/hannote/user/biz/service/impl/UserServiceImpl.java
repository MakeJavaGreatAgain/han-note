package com.hanserwei.hannote.user.biz.service.impl;


import cn.dev33.satoken.stp.StpUtil;
import com.google.common.base.Preconditions;
import com.hanserwei.framework.constant.RedisKeyConstants;
import com.hanserwei.framework.exception.BizException;
import com.hanserwei.framework.response.Response;
import com.hanserwei.framework.utils.ParamUtils;
import com.hanserwei.hannote.user.biz.domain.dataobject.RoleDO;
import com.hanserwei.hannote.user.biz.domain.dataobject.UserRoleRelDO;
import com.hanserwei.hannote.user.biz.domain.mapper.RoleDOMapper;
import com.hanserwei.hannote.user.biz.domain.mapper.UserRoleRelDOMapper;
import com.hanserwei.hannote.user.biz.model.vo.UpdatePasswordReqVO;
import com.hanserwei.hannote.biz.context.holer.LoginUserContextHolder;
import com.hanserwei.hannote.user.biz.domain.dataobject.UserDO;
import com.hanserwei.hannote.user.biz.domain.mapper.UserDOMapper;
import com.hanserwei.hannote.user.biz.enums.ResponseCodeEnum;
import com.hanserwei.hannote.user.biz.enums.SexEnum;
import com.hanserwei.hannote.user.biz.model.vo.UpdateUserInfoReqVO;
import com.hanserwei.hannote.user.biz.rpc.OssRpcService;
import com.hanserwei.hannote.user.biz.service.UserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Strings;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * @author hanserwei
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Resource
    private UserDOMapper userDOMapper;

    @Resource
    private OssRpcService ossRpcService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private UserRoleRelDOMapper userRoleRelDOMapper;
    @Resource
    private RoleDOMapper roleDOMapper;

    /**
     * 更新用户信息
     *
     * @param updateUserInfoReqVO 更新用户信息请求
     * @return 更新结果
     */
    @Override
    public Response<?> updateUserInfo(UpdateUserInfoReqVO updateUserInfoReqVO) {
        Long userId = LoginUserContextHolder.getUserId();
        Preconditions.checkArgument(Objects.nonNull(userId), "用户未登录");

        UserDO userDO = UserDO.builder()
                .id(userId)
                .build();
        boolean needUpdate = false;

        MultipartFile avatarFile = updateUserInfoReqVO.avatar();
        if (Objects.nonNull(avatarFile) && !avatarFile.isEmpty()) {
            userDO.setAvatar(uploadFile(avatarFile, ResponseCodeEnum.UPLOAD_AVATAR_FAIL));
            needUpdate = true;
        }

        String nickname = StringUtils.trim(updateUserInfoReqVO.nickname());
        if (StringUtils.isNotBlank(nickname)) {
            Preconditions.checkArgument(ParamUtils.checkNickname(nickname),
                    ResponseCodeEnum.NICK_NAME_VALID_FAIL.getErrorMessage());
            userDO.setNickname(nickname);
            needUpdate = true;
        }

        String hannoteId = StringUtils.trim(updateUserInfoReqVO.hannoteId());
        if (StringUtils.isNotBlank(hannoteId)) {
            Preconditions.checkArgument(ParamUtils.checkHanNoteId(hannoteId),
                    ResponseCodeEnum.HANNOTE_ID_VALID_FAIL.getErrorMessage());
            userDO.setHannoteId(hannoteId);
            needUpdate = true;
        }

        Integer sex = updateUserInfoReqVO.sex();
        if (Objects.nonNull(sex)) {
            Preconditions.checkArgument(SexEnum.isValid(sex), ResponseCodeEnum.SEX_VALID_FAIL.getErrorMessage());
            userDO.setSex(sex);
            needUpdate = true;
        }

        LocalDate birthday = updateUserInfoReqVO.birthday();
        if (Objects.nonNull(birthday)) {
            userDO.setBirthday(birthday);
            needUpdate = true;
        }

        String introduction = StringUtils.trim(updateUserInfoReqVO.introduction());
        if (StringUtils.isNotBlank(introduction)) {
            Preconditions.checkArgument(ParamUtils.checkLength(introduction, 100),
                    ResponseCodeEnum.INTRODUCTION_VALID_FAIL.getErrorMessage());
            userDO.setIntroduction(introduction);
            needUpdate = true;
        }

        MultipartFile backgroundImgFile = updateUserInfoReqVO.backgroundImg();
        if (Objects.nonNull(backgroundImgFile) && !backgroundImgFile.isEmpty()) {
            userDO.setBackgroundImg(uploadFile(backgroundImgFile, ResponseCodeEnum.UPLOAD_BACKGROUND_IMG_FAIL));
            needUpdate = true;
        }

        if (!needUpdate) {
            return Response.success();
        }

        userDO.setUpdateTime(LocalDateTime.now());
        userDOMapper.updateByPrimaryKeySelective(userDO);
        log.info("update user info success, userId: {}", userId);
        return Response.success();
    }

    /**
     * 上传文件
     *
     * @param file             文件
     * @param responseCodeEnum 上传失败响应码
     * @return 文件访问链接
     */
    private String uploadFile(MultipartFile file, ResponseCodeEnum responseCodeEnum) {
        String fileUrl = ossRpcService.uploadFile(file);
        if (StringUtils.isBlank(fileUrl)) {
            throw new BizException(responseCodeEnum);
        }
        return fileUrl;
    }
    @Override
    public Response<?> updatePassword(UpdatePasswordReqVO vo) {
        String phone = vo.phone();
        String code = vo.verificationCode();

        // 1. 验证码校验
        Preconditions.checkArgument(StringUtils.isNotBlank(code), "验证码不能为空");
        String key = RedisKeyConstants.buildUpdatePasswordVerificationCodeKey(phone);
        String sentCode = (String) redisTemplate.opsForValue().get(key);

        if (StringUtils.isBlank(sentCode)) {
            throw new BizException(ResponseCodeEnum.VERIFICATION_CODE_EXPIRED);
        }
        if (!Strings.CS.equals(code, sentCode)) {
            throw new BizException(ResponseCodeEnum.VERIFICATION_CODE_ERROR);
        }

        // 2. 更新密码
        Long userId = LoginUserContextHolder.getUserId();
        UserDO userDO = UserDO.builder()
                .id(userId)
                .password(passwordEncoder.encode(vo.newPassword()))
                .updateTime(LocalDateTime.now())
                .build();

        userDOMapper.updateByPrimaryKeySelective(userDO);

        // 3. 清理验证码，防止二次使用
        redisTemplate.delete(key);

        // 4. 登出
        StpUtil.logout();

        return Response.success();
    }

    @Override
    public Response<?> selectByPhone(String phone) {
        UserDO userDO = userDOMapper.selectByPhone(phone);
        return Response.success(userDO);
    }

    @Override
    public Response<?> insertUser(UserDO userDO) {
        userDOMapper.insert(userDO);
        return Response.success();
    }

    @Override
    public Response<?> insertUserRoleRel(UserRoleRelDO userRoleDO) {
        userRoleRelDOMapper.insert(userRoleDO);
        return Response.success();
    }

    @Override
    public Response<?> selectUserRoleRelByUserId(Long userId) {
        List<Long> userRoles = userRoleRelDOMapper.selectByUserId(userId);
        return Response.success(userRoles);
    }

    @Override
    public Response<?> selectByPrimaryKey(Long primaryKey) {
        RoleDO roleDO = roleDOMapper.selectByPrimaryKey(primaryKey);
        return Response.success(roleDO);
    }

    @Override
    public Response<?> selectRoleKeyListByIdList(List<Long> roleIdList) {
        List<String> roleKeys = roleDOMapper.selectRoleKeyListByIdList(roleIdList);
        return Response.success(roleKeys);
    }
}
