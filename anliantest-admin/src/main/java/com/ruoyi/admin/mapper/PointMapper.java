package com.ruoyi.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.admin.entity.Point;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author zhanghao
 * @date 2023-06-09
 * @desc : 车间点位
 */
@Mapper
public interface PointMapper extends BaseMapper<Point> {
    /**
     * 根据车间岗位排序查询点位 （用于初始化点位编号）
     * @param planId
     * @return
     */
    @Select("select po.* from ly_point po\n" +
            "left join ly_project_workshop  pw on po.workshop_id=pw.id\n" +
            "left join ly_project_workshop  pw2 on pw2.id=pw.pid\n" +
            "left join ly_project_workshop  pw3 on pw3.id=pw2.pid\n" +
            "where po.plan_id=#{planId }\n" +
            "ORDER BY pw3.id,pw2.id,pw.id,po.id\n ")
    List<Point> pointList(Long planId);
}
