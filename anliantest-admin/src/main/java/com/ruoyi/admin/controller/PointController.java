package com.ruoyi.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.admin.entity.Point;
import com.ruoyi.admin.service.PointService;
import com.ruoyi.common.annotation.OperateLog;
import com.ruoyi.common.exception.RRException;
import com.ruoyi.common.utils.R;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author zhanghao
 * @date 2023-06-14
 * @desc : 项目点位
 */
@RestController
@Api(tags = "项目点位")
@RequestMapping("/liangyuan/point")
public class PointController {


    private PointService pointService;

    @Autowired
    public PointController(PointService pointService) {
        this.pointService = pointService;
    }


    /**
     * 新增项目点位
     *
     * @param point
     * @return
     */
    @PostMapping("save")
    @OperateLog(title = "新增项目点位")
    public R save(@RequestBody Point point) {
        verify(point);
        //查询最大点位编号
        Point one = pointService.getOne(new LambdaQueryWrapper<Point>().eq(Point::getPlanId,point.getPlanId()).orderByDesc(Point::getId).last(" limit 1 "));
        Integer i=1;
        if(one!=null){
            i=one.getCodeNumber()+1;
        }
        point.setCodeNumber(i);
        String s="";
        if(i<10){
            s="00"+i;
        }else if(i<100){
            s="0"+i;
        }else {
            s=String.valueOf(i);
        }
        point.setSpotCode(s);
        pointService.save(point);
        return R.ok("新增成功");
    }


    /**
     * 修改项目点位
     *
     * @param point
     * @return
     */
    @OperateLog(title = "修改项目点位")
    @PostMapping("update")
    public R update(@RequestBody Point point) {
        verify(point);

        pointService.updateById(point);
        return R.ok("修改成功");
    }

    /**
     * 删除项目点位
     */
    @DeleteMapping("/del")
    @OperateLog(title = "删除项目点位")
    public R remove(@RequestBody Point point) {

        pointService.removeById(point.getId());

        //重新生成点位编号
        pointService.initializationPoint(point.getPlanId());
        return R.ok("删除成功");
    }

    //重复验证
    public void verify(Point point) {

        int count = pointService.count(new LambdaQueryWrapper<Point>()
                .eq(Point::getWorkshopId, point.getWorkshopId())
                .eq(Point::getPoint, point.getPoint())

        );
        if (count > 0) {
            throw new RRException("点位不能重复");
        }

    }


}
