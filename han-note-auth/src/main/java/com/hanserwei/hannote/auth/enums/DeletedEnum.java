package com.hanserwei.hannote.auth.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author hanserwei
 */

@Getter
@AllArgsConstructor
public enum DeletedEnum {

    /**
     * 已删除
     */
    YES(true),
    /**
     * 未删除
     */
    NO(false);

    private final Boolean value;
}