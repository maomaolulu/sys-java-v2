package com.ruoyi.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @Description
 * @Date 2023/4/25 15:09
 * @Author maoly
 **/
@Getter
@RequiredArgsConstructor
public enum ClientType {

    PC("PC","PC端"),


    APP("PC","APP端");

    /**
     * 类型
     */
    private final String type;

    /**
     * 描述
     */
    private final String description;
}
