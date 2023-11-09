package com.ruoyi.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.admin.entity.IndustryEntity;

import java.util.List;

public interface IndustryService extends IService<IndustryEntity> {
    /**
     * 获取行业类别列表
     * @param joint
     * @return
     */
    List<IndustryEntity> listJoint(String joint);
}
