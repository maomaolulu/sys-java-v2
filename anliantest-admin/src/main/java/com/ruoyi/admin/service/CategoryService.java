package com.ruoyi.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.admin.entity.CategoryEntity;

import java.util.List;

/**
 * @author ZhuYiCheng
 * @date 2023/4/4 10:45
 */
public interface CategoryService extends IService<CategoryEntity> {
    /**
     * 获取类型信息列表
     */
    List<CategoryEntity> getCategory();
}
