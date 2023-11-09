package com.ruoyi.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @Description 物质检测方法表 是否需要吸收液（为了提前通知实验室）
 * @Date 2023/6/6
 * @Author gy
 **/
@Getter
@RequiredArgsConstructor
public enum IsAbsorbentSolutionType {
    /**
     *  是
     */
    YES(1,"是"),

    /**
     * 否
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
