package com.ruoyi.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @Description 物质检测方法表 是否支持个体采样
 * @Date 2023/6/6
 * @Author gy
 **/
@Getter
@RequiredArgsConstructor
public enum IsPersonalSampleType {
    /**
     * 支持
     */
    YES(1,"是"),

    /**
     * 不支持
     */
    NO(0,"否");

    /**
     * 类型
     */
    private final Integer code;
    /**
     * 描述
     */
    private final String description;
}
