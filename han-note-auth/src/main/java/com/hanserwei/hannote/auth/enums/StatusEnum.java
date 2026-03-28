package com.hanserwei.hannote.auth.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author hanserwei
 */

@Getter
@AllArgsConstructor
public enum StatusEnum {
    /**
     * 启用
     */
    ENABLE(0),
    /**
     * 禁用
     */
    DISABLED(1);

    private final Integer value;
}