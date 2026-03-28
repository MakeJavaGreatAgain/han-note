package com.hanserwei.hannote.auth.service.impl;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.hanserwei.framework.exception.BizException;
import com.hanserwei.framework.response.Response;
import com.hanserwei.framework.utils.JsonUtils;
import com.hanserwei.hannote.auth.constant.RedisKeyConstants;
import com.hanserwei.hannote.auth.constant.RoleConstants;
import com.hanserwei.hannote.auth.domain.dataobject.UserDO;
import com.hanserwei.hannote.auth.domain.dataobject.UserRoleRelDO;
import com.hanserwei.hannote.auth.domain.mapper.UserDOMapper;
import com.hanserwei.hannote.auth.domain.mapper.UserRoleRelDOMapper;
import com.hanserwei.hannote.auth.enums.DeletedEnum;
import com.hanserwei.hannote.auth.enums.LoginTypeEnum;
import com.hanserwei.hannote.auth.enums.ResponseCodeEnum;
import com.hanserwei.hannote.auth.enums.StatusEnum;
import com.hanserwei.hannote.auth.model.vo.user.UserLoginReqVO;
import com.hanserwei.hannote.auth.service.UserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Strings;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * @author hanserwei
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserDOMapper userDOMapper;
    @Resource
    private UserRoleRelDOMapper userRoleRelDOMapper;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private TransactionTemplate transactionTemplate;

    @Override
    public Response<String> loginAndRegister(UserLoginReqVO userLoginReqVO) {
        String phone = userLoginReqVO.phone();
        Integer loginType = userLoginReqVO.type();

        LoginTypeEnum typeEnum = LoginTypeEnum.valueOf(loginType);

        Long userId = null;

        Assert.notNull(typeEnum, "登录方式不能为空！");
        switch (typeEnum) {
            case VERIFICATION_CODE -> {
                String verificationCode = userLoginReqVO.code();
                Preconditions.checkArgument(StringUtils.isNotBlank(verificationCode), "验证码不能为空");
                // 构建验证码 Redis Key
                String key = RedisKeyConstants.buildVerificationCodeKey(phone);
                // 查询存储在 Redis 中该用户的登录验证码
                String sentCode = (String) redisTemplate.opsForValue().get(key);

                // 判断用户提交的验证码，与 Redis 中的验证码是否一致
                if (!Strings.CS.equals(verificationCode, sentCode)) {
                    throw new BizException(ResponseCodeEnum.VERIFICATION_CODE_ERROR);
                }

                // 通过手机号查询记录
                UserDO userDO = userDOMapper.selectByPhone(phone);

                log.info("==> 用户是否注册, phone: {}, userDO: {}", phone, JsonUtils.toJsonString(userDO));

                // 判断是否注册
                if (Objects.isNull(userDO)) {
                    // 若此用户还没有注册，系统自动注册该用户
                    userId = registerUser(phone);

                } else {
                    // 已注册，则获取其用户 ID
                    userId = userDO.getId();
                }
            }
            case PASSWORD -> {

            }

            default -> {

            }
        }

        // SaToken 登录用户, 入参为用户 ID
        StpUtil.login(userId);

        // 获取 Token 令牌
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();

        // 返回 Token 令牌
        return Response.success(tokenInfo.tokenValue);
    }

    /**
     * 用户注册
     *
     * @param phone 手机号
     * @return 用户 ID
     */
    private Long registerUser(String phone) {
        return transactionTemplate.execute(status -> {
            try {
                // 获取全局自增的小憨书ID
                Long hannoteId = redisTemplate.opsForValue().increment(RedisKeyConstants.HANNOTE_ID_GENERATOR_KEY);

                UserDO userDO = UserDO.builder()
                        .phone(phone)
                        .hannoteId(String.valueOf(hannoteId))
                        .nickname("小憨憨" + hannoteId)
                        .status(StatusEnum.ENABLE.getValue())
                        .createTime(LocalDateTime.now())
                        .updateTime(LocalDateTime.now())
                        .deleted(DeletedEnum.NO.getValue())
                        .build();

                // 入库
                userDOMapper.insert(userDO);
                // 获取刚刚添加入库的用户 ID
                Long userId = userDO.getId();

                // 给该用户分配一个默认角色
                UserRoleRelDO userRoleDO = UserRoleRelDO.builder()
                        .userId(userId)
                        .roleId(RoleConstants.COMMON_USER_ROLE_ID)
                        .createTime(LocalDateTime.now())
                        .updateTime(LocalDateTime.now())
                        .deleted(DeletedEnum.NO.getValue())
                        .build();
                userRoleRelDOMapper.insert(userRoleDO);

                // 将该用户的角色 ID 存入 Redis 中
                List<Long> roles = Lists.newArrayList();
                roles.add(RoleConstants.COMMON_USER_ROLE_ID);
                String userRolesKey = RedisKeyConstants.buildUserRoleKey(phone);
                redisTemplate.opsForValue().set(userRolesKey, JsonUtils.toJsonString(roles));

                return userId;
            } catch (Exception e) {
                status.setRollbackOnly();
                log.error("==> 系统注册用户异常: ", e);
                return null;
            }
        });
    }
}
