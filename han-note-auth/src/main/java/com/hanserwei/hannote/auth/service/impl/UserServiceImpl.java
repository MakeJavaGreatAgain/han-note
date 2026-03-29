package com.hanserwei.hannote.auth.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.google.common.base.Preconditions;
import com.hanserwei.framework.exception.BizException;
import com.hanserwei.framework.response.Response;
import com.hanserwei.framework.utils.JsonUtils;
import com.hanserwei.hannote.auth.constant.RedisKeyConstants;
import com.hanserwei.hannote.auth.constant.RoleConstants;
import com.hanserwei.hannote.auth.domain.dataobject.RoleDO;
import com.hanserwei.hannote.auth.domain.dataobject.UserDO;
import com.hanserwei.hannote.auth.domain.dataobject.UserRoleRelDO;
import com.hanserwei.hannote.auth.domain.mapper.RoleDOMapper;
import com.hanserwei.hannote.auth.domain.mapper.UserDOMapper;
import com.hanserwei.hannote.auth.domain.mapper.UserRoleRelDOMapper;
import com.hanserwei.hannote.auth.enums.DeletedEnum;
import com.hanserwei.hannote.auth.enums.LoginTypeEnum;
import com.hanserwei.hannote.auth.enums.ResponseCodeEnum;
import com.hanserwei.hannote.auth.enums.StatusEnum;
import com.hanserwei.hannote.auth.model.vo.user.UpdatePasswordReqVO;
import com.hanserwei.hannote.auth.model.vo.user.UserLoginReqVO;
import com.hanserwei.hannote.auth.service.UserService;
import com.hanserwei.hannote.biz.context.holer.LoginUserContextHolder;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Strings;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author hanserwei
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserDOMapper userDOMapper;
    @Resource
    private RoleDOMapper roleDOMapper;
    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private UserRoleRelDOMapper userRoleRelDOMapper;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private TransactionTemplate transactionTemplate;

    /**
     * 用户登录注册入口
     */
    @Override
    public Response<String> loginAndRegister(UserLoginReqVO vo) {
        LoginTypeEnum typeEnum = LoginTypeEnum.valueOf(vo.type());
        if (Objects.isNull(typeEnum)) {
            throw new BizException(ResponseCodeEnum.LOGIN_TYPE_ERROR);
        }

        // 1. 使用 Java 17 Switch 表达式分流逻辑
        Long userId = switch (typeEnum) {
            case VERIFICATION_CODE -> handleVerificationCodeLogin(vo);
            case PASSWORD -> handlePasswordLogin(vo);
        };

        // 2. 执行 Sa-Token 登录
        StpUtil.login(userId);

        // 3. 返回 Token
        return Response.success(StpUtil.getTokenValue());
    }

    /**
     * 处理：验证码登录/自动注册
     */
    private Long handleVerificationCodeLogin(UserLoginReqVO vo) {
        String phone = vo.phone();
        String code = vo.code();
        Preconditions.checkArgument(StringUtils.isNotBlank(code), "验证码不能为空");

        // 校验 Redis 验证码
        String key = RedisKeyConstants.buildVerificationCodeKey(phone);
        String sentCode = (String) redisTemplate.opsForValue().get(key);
        if (!Strings.CS.equals(code, sentCode)) {
            throw new BizException(ResponseCodeEnum.VERIFICATION_CODE_ERROR);
        }

        UserDO userDO = userDOMapper.selectByPhone(phone);
        if (Objects.isNull(userDO)) {
            // 未注册则走自动注册流程
            return registerUser(phone);
        }

        // 已注册用户，确保角色缓存同步
        syncUserRolesToRedis(userDO.getId());
        return userDO.getId();
    }

    /**
     * 处理：密码登录
     */
    private Long handlePasswordLogin(UserLoginReqVO vo) {
        UserDO userDO = userDOMapper.selectByPhone(vo.phone());
        if (Objects.isNull(userDO)) {
            throw new BizException(ResponseCodeEnum.USER_NOT_FOUND);
        }

        // 密码匹配
        if (!passwordEncoder.matches(vo.password(), userDO.getPassword())) {
            throw new BizException(ResponseCodeEnum.PHONE_OR_PASSWORD_ERROR);
        }

        syncUserRolesToRedis(userDO.getId());
        return userDO.getId();
    }

    /**
     * 核心逻辑提取：同步用户角色到 Redis 缓存
     * 设置 24 小时过期时间，防止角色权限变动后的“脏数据”永驻
     */
    private void syncUserRolesToRedis(Long userId) {
        String userRolesKey = RedisKeyConstants.buildUserRoleKey(String.valueOf(userId));

        // 如果 Redis 中已经有了，且不考虑立即生效需求，可以直接跳过以节省数据库压力
        if (Boolean.TRUE.equals(redisTemplate.hasKey(userRolesKey))) {
            return;
        }

        log.info("==> 开始同步用户角色至 Redis, userId: {}", userId);
        List<Long> roleIdList = userRoleRelDOMapper.selectByUserId(userId);
        List<String> roleKeyList = roleDOMapper.selectRoleKeyListByIdList(roleIdList);

        // 写入缓存并设置 TTL (24小时)
        redisTemplate.opsForValue().set(userRolesKey, JsonUtils.toJsonString(roleKeyList), 24, TimeUnit.HOURS);
    }

    /**
     * 事务化注册新用户
     */
    private Long registerUser(String phone) {
        return transactionTemplate.execute(status -> {
            try {
                // 1. 生成全局唯一 ID
                Long hannoteId = redisTemplate.opsForValue().increment(RedisKeyConstants.HANNOTE_ID_GENERATOR_KEY);

                // 2. 创建用户
                UserDO userDO = UserDO.builder()
                        .phone(phone)
                        .hannoteId(String.valueOf(hannoteId))
                        .nickname("小憨憨" + hannoteId)
                        .status(StatusEnum.ENABLE.getValue())
                        .createTime(LocalDateTime.now())
                        .updateTime(LocalDateTime.now())
                        .deleted(DeletedEnum.NO.getValue())
                        .build();
                userDOMapper.insert(userDO);
                Long userId = userDO.getId();

                // 3. 绑定默认角色
                UserRoleRelDO userRoleDO = UserRoleRelDO.builder()
                        .userId(userId)
                        .roleId(RoleConstants.COMMON_USER_ROLE_ID)
                        .createTime(LocalDateTime.now())
                        .updateTime(LocalDateTime.now())
                        .deleted(DeletedEnum.NO.getValue())
                        .build();
                userRoleRelDOMapper.insert(userRoleDO);

                // 4. 立即同步角色到 Redis
                RoleDO roleDO = roleDOMapper.selectByPrimaryKey(RoleConstants.COMMON_USER_ROLE_ID);
                List<String> roles = List.of(roleDO.getRoleKey());
                redisTemplate.opsForValue().set(RedisKeyConstants.buildUserRoleKey(String.valueOf(userId)),
                        JsonUtils.toJsonString(roles), 24, TimeUnit.HOURS);

                return userId;
            } catch (Exception e) {
                status.setRollbackOnly();
                log.error("==> 用户注册事务失败: ", e);
                throw new BizException(ResponseCodeEnum.SYSTEM_ERROR);
            }
        });
    }

    @Override
    public Response<?> logout() {
        Long userId = LoginUserContextHolder.getUserId();
        StpUtil.logout(userId);
        return Response.success();
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
}
