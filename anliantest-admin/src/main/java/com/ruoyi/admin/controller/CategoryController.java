package com.ruoyi.admin.controller;

import com.ruoyi.admin.entity.CategoryEntity;
import com.ruoyi.admin.service.CategoryService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author ZhuYiCheng
 * @date 2023/4/4 10:43
 */
@RestController
@Api(tags = "类型信息")
@RequestMapping("/liangyuan/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 获取类型信息列表
     */
    @GetMapping("/getCategory")
    public List<CategoryEntity> getCategory(){
        List<CategoryEntity> categoryEntityList=categoryService.getCategory();
        return categoryEntityList;
    }


}
