package com.hanserwei.hannote.user.biz.domain.dataobject;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author hanserwei
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDO {
    /**
     * 用户ID
     */
    private Long id;

    /**
     * 小憨书ID
     */
    private String hannoteId;

    /**
     * 密码
     */
    private String password;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 生日
     */
    private LocalDate birthday;

    /**
     * 背景图片
     */
    private String backgroundImg;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 性别(0：女，1：男)
     */
    private Integer sex;

    /**
     * 状态(0：启用，1：禁用)
     */
    private Integer status;

    /**
     * 个人简介
     */
    private String introduction;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 是否删除(删除：true，未删除：false)
     */
    private Boolean deleted;
}
