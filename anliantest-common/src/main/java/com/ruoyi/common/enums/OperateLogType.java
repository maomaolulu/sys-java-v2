package com.ruoyi.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @Description 日志类型
 * @Date 2023/4/25 14:41
 * @Author maoly
 **/
@Getter
@RequiredArgsConstructor
public enum OperateLogType {
    /**
     * 正常日志类型
     */
    NORMAL_LOG("0", "正常日志"),

    /**
     * 错误日志类型
     */
    EXCEPTION_LOG("1", "异常日志");

    /**
     * 类型
     */
    private final String type;

    /**
     * 描述
     */
    private final String description;
}
