package com.ruoyi.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @Description 物质检测方法表 是否支持定点采样
 * @Date 2023/6/6
 * @Author gy
 **/
@Getter
@RequiredArgsConstructor
public enum IsAreaSampleType {
    /**
     *  支持
     */
    YES(1,"支持"),

    /**
     * 不支持
     */
    NO(0,"不支持");

    /**
     * 类型
     */
    private final Integer code;
    /**
     * 描述
     */
    private final String description;
}
