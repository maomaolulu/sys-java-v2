package com.ruoyi.common.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author ANLIAN
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RepeatSubmit {

    /**
     * 延迟多少秒后可以重复提交，默认5s
     * @return
     */

    long expire() default 5;

}
