package com.hanserwei.hannote.user.biz.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * @author hanserwei
 */

@Getter
@AllArgsConstructor
public enum SexEnum {

    /**
     * 性别
     */
    WOMAN(0),
    MAN(1);

    /**
     * 性别，0：女，1：男
     */
    private final Integer value;

    /**
     * 判断是否有效
     *
     * @param value value
     * @return boolean
     */
    public static boolean isValid(Integer value) {
        for (SexEnum loginTypeEnum : SexEnum.values()) {
            if (Objects.equals(value, loginTypeEnum.getValue())) {
                return true;
            }
        }
        return false;
    }

}