package com.ruoyi.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.admin.entity.CategoryEntity;
import com.ruoyi.admin.mapper.CategoryMapper;
import com.ruoyi.admin.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ZhuYiCheng
 * @date 2023/4/4 10:45
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, CategoryEntity> implements CategoryService {

    /**
     * 获取类型信息列表
     */
    @Override
    public List<CategoryEntity> getCategory() {
        List<CategoryEntity> categoryEntityList = this.list();
        return categoryEntityList;
    }
}
