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
import com.hanserwei.hannote.auth.model.vo.user.UserLoginReqVO;
import com.hanserwei.hannote.auth.service.UserService;
import com.hanserwei.hannote.biz.context.holer.LoginUserContextHolder;
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
    private RoleDOMapper roleDOMapper;
    @Resource
    private UserRoleRelDOMapper userRoleRelDOMapper;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private TransactionTemplate transactionTemplate;

    /**
     * 用户登录注册
     *
     * @param userLoginReqVO 登录参数
     * @return Token 令牌
     */
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
                    // 获取并设置用户角色
                    // 1.查询 redis 是否有该用户的角色信息
                    String userRolesKey = RedisKeyConstants.buildUserRoleKey(String.valueOf(userId));
                    Object userRolesObj = redisTemplate.opsForValue().get(userRolesKey);
                    if (Objects.nonNull(userRolesObj)) {
                        // 如果 Redis 中有该用户角色信息，可以后续使用
                        log.info("==> 用户角色信息已存在 Redis 中，userId: {}", userId);
                        break;
                    }
                    log.info("==> 用户角色信息不存在 Redis 中，userId: {}", userId);
                    List<Long> roleIdList = userRoleRelDOMapper.selectByUserId(userId);
                    // 根据角色 ID 列表查询角色列表
                    List<String> roleKeyList = roleDOMapper.selectRoleKeyListByIdList(roleIdList);
                    // 将用户角色信息存入 Redis 中
                    redisTemplate.opsForValue().set(userRolesKey, JsonUtils.toJsonString(roleKeyList));
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

                RoleDO roleDO = roleDOMapper.selectByPrimaryKey(RoleConstants.COMMON_USER_ROLE_ID);

                // 将该用户的角色 ID 存入 Redis 中，指定初始容量为 1，这样可以减少在扩容时的性能开销
                List<String> roles = Lists.newArrayListWithCapacity(1);
                roles.add(roleDO.getRoleKey());

                String userRolesKey = RedisKeyConstants.buildUserRoleKey(String.valueOf(userId));
                redisTemplate.opsForValue().set(userRolesKey, JsonUtils.toJsonString(roles));

                return userId;
            } catch (Exception e) {
                status.setRollbackOnly();
                log.error("==> 系统注册用户异常: ", e);
                return null;
            }
        });
    }

    /**
     * 用户登出
     *
     * @return 是否登出成功
     */
    @Override
    public Response<?> logout() {
        Long userId = LoginUserContextHolder.getUserId();
        // 退出登录 (指定用户 ID)
        StpUtil.logout(userId);
        return Response.success();
    }
}
