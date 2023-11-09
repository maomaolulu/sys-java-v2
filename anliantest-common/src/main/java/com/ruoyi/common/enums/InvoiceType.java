package com.ruoyi.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author gy
 * @date 2023-06-09 9:02
 */
@Getter
@RequiredArgsConstructor
public enum InvoiceType {
    /**
     * 增值税专用发票
     */
    ONE(1,"增值税专用发票"),

    /**
     * 增值税普通发票
     */
    ZERO(0,"增值税普通发票");

    /**
     * 类型
     */
    private final Integer code;
    /**
     * 描述
     */
    private final String description;
}
