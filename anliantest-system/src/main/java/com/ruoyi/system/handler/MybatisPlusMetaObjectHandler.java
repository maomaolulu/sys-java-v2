package com.ruoyi.system.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.login.utils.ShiroUtils;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

/**
 * 字段自动填充器
 *
 * @author ZhuYiCheng
 * @date 2023/6/25 14:23
 */
@Component
public class MybatisPlusMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {

        this.setFieldValByName( "createBy", ShiroUtils.getUserName(),metaObject);
        this.setFieldValByName("createTime", DateUtils.getNowDate(),metaObject);
        this.setFieldValByName("updateBy", ShiroUtils.getUserName(),metaObject);
        this.setFieldValByName("updateTime",DateUtils.getNowDate(),metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updateBy",ShiroUtils.getUserName(),metaObject);
        this.setFieldValByName("updateTime",DateUtils.getNowDate(),metaObject);
    }
}
