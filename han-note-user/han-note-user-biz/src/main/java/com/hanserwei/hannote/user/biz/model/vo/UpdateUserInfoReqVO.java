package com.hanserwei.hannote.user.biz.model.vo;

import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

/**
 * @author hanserwei
 */
public record UpdateUserInfoReqVO(
        /*
          头像文件
         */
        MultipartFile avatar,

        /*
          昵称
         */
        String nickname,

        /*
          小哈书 (小憨书) ID
         */
        String hannoteId,

        /*
          性别 (0：女，1：男)
         */
        Integer sex,

        /*
          生日
         */
        LocalDate birthday,

        /*
          个人介绍
         */
        String introduction,

        /*
          背景图文件
         */
        MultipartFile backgroundImg
) {
}
