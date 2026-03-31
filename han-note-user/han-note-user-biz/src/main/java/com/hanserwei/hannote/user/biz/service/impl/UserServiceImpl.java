package com.hanserwei.hannote.user.biz.service.impl;

import com.google.common.base.Preconditions;
import com.hanserwei.framework.response.Response;
import com.hanserwei.framework.utils.ParamUtils;
import com.hanserwei.hannote.biz.context.holer.LoginUserContextHolder;
import com.hanserwei.hannote.user.biz.domain.dataobject.UserDO;
import com.hanserwei.hannote.user.biz.domain.mapper.UserDOMapper;
import com.hanserwei.hannote.user.biz.enums.ResponseCodeEnum;
import com.hanserwei.hannote.user.biz.enums.SexEnum;
import com.hanserwei.hannote.user.biz.model.vo.UpdateUserInfoReqVO;
import com.hanserwei.hannote.user.biz.service.UserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author hanserwei
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Resource
    private UserDOMapper userDOMapper;

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
            // todo: 调用对象存储服务上传文件，回填头像地址并将 needUpdate 置为 true
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
            // todo: 调用对象存储服务上传文件，回填背景图地址并将 needUpdate 置为 true
        }

        if (!needUpdate) {
            return Response.success();
        }

        userDO.setUpdateTime(LocalDateTime.now());
        userDOMapper.updateByPrimaryKeySelective(userDO);
        log.info("update user info success, userId: {}", userId);
        return Response.success();
    }
}
