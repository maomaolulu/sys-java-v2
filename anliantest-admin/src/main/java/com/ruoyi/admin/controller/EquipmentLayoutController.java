package com.ruoyi.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.admin.entity.EquipmentLayout;
import com.ruoyi.admin.service.EquipmentLayoutService;
import com.ruoyi.common.annotation.OperateLog;
import com.ruoyi.common.exception.RRException;
import com.ruoyi.common.utils.R;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author zhanghao
 * @date 2023-06-12
 * @desc : 设备布局
 */
@RestController
@Api(tags = "设备布局")
@RequestMapping("/liangyuan/equipment_layout")
public class EquipmentLayoutController {


    private EquipmentLayoutService equipmentLayoutService;

    @Autowired
    public EquipmentLayoutController(EquipmentLayoutService equipmentLayoutService) {
        this.equipmentLayoutService = equipmentLayoutService;
    }


    /**
     * 设备布局列表
     *
     * @param planId
     * @return
     */
    @GetMapping("/list")
    public R list(Long planId) {

        return R.data(equipmentLayoutService.equipmentLayoutList(planId));
    }

    /**
     * 新增设备布局
     *
     * @param equipmentLayout
     * @return
     */
    @PostMapping("/save")
    @OperateLog(title = "新增设备布局")
    public R save(@RequestBody EquipmentLayout equipmentLayout) {
        verify(equipmentLayout);
        equipmentLayoutService.save(equipmentLayout);
        return R.ok("新增成功");
    }


    /**
     * 修改设备布局
     *
     * @param equipmentLayout
     * @return
     */
    @OperateLog(title = "修改设备布局")
    @PostMapping("/update")
    public R update(@RequestBody EquipmentLayout equipmentLayout) {
        verify(equipmentLayout);

        equipmentLayoutService.updateById(equipmentLayout);
        return R.ok("修改成功");
    }

    /**
     * 删除设备布局
     */
    @DeleteMapping("/del")
    @OperateLog(title = "删除设备布局")
    public R remove(@RequestBody List<Long> ids) {

        equipmentLayoutService.removeByIds(ids);
        return R.ok("删除成功");
    }


    //重复验证
    public void verify(EquipmentLayout equipmentLayout) {

        int count = equipmentLayoutService.count(new LambdaQueryWrapper<EquipmentLayout>()
                .eq(EquipmentLayout::getWorkshopId, equipmentLayout.getWorkshopId())

                .eq(EquipmentLayout::getEquipment, equipmentLayout.getEquipment())

        );
        if (count > 0) {
            throw new RRException("设备名称不能重复");
        }
    }

}
