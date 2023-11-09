package com.ruoyi.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.admin.entity.Chinas;

import java.util.List;

public interface ChinasService extends IService<Chinas> {

    List<Chinas> getRegions();

    /**
     * 导入省市区
     * @param chinasList
     * @return
     */
    Boolean importRegions(List<Chinas> chinasList);

//    Boolean import1();

}