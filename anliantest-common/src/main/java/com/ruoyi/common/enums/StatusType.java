package com.ruoyi.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 物质检测标准法律法规表状态
 *
 * @author gy
 *
 */
@Getter
@RequiredArgsConstructor
public enum StatusType {
    /**
     * 现行
     */
    ONE(1,"现行"),

    /**
     * 废止
     */
    ZERO(0,"废止");

    /**
     * 类型
     */
    private final Integer code;
    /**
     * 描述
     */
    private final String description;

}
