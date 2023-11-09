package com.ruoyi.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.admin.entity.Point;
import com.ruoyi.admin.mapper.PointMapper;
import com.ruoyi.admin.service.PointService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zhanghao
 * @date 2023-06-09
 * @desc : 车间点位
 */
@Service
public class PointServiceImpl extends ServiceImpl<PointMapper, Point> implements PointService {
    /**
     * 初始化点位编号
     * @param planId
     * @return
     */
    @Override
    public void initializationPoint(Long planId) {

        List<Point> points = baseMapper.pointList(planId);

        if(CollUtil.isNotEmpty(points)){
            int i=1;
            for (Point point:points
                 ) {

                String s="";
                if(i<10){
                    s="00"+i;
                }else if(i<100){
                    s="0"+i;
                }else {
                    s=String.valueOf(i);
                }
                point.setCodeNumber(i);
                point.setSpotCode(s);
                i++;
            }

        }
    }
}