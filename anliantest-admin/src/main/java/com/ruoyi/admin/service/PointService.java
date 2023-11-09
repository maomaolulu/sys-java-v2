package com.ruoyi.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.admin.entity.Point;

import java.util.List;

/**
 * @author zhanghao
 * @date 2023-06-09
 * @desc : 车间点位 */
public interface PointService extends IService<Point> {

    void initializationPoint(Long planId);
}