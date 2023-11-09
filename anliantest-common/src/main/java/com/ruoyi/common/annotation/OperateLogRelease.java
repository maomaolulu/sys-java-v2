package com.ruoyi.common.annotation;

import java.lang.annotation.*;

/**
 * @Description 操作日志
 * @Date 2023/4/25 13:46
 * @Author maoly
 **/
@Target({ ElementType.PARAMETER, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperateLogRelease {

    /**
     * 模块
     */
    public String title() default "";

    /**
     * 是否保存请求的参数
     */
    public boolean isSaveRequestData() default true;
}
