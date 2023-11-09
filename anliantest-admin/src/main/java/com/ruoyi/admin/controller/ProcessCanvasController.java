package com.ruoyi.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.admin.domain.dto.ProcessCanvasDto;
import com.ruoyi.admin.entity.ProcessCanvasEntity;
import com.ruoyi.admin.service.ProcessCanvasService;
import com.ruoyi.common.annotation.OperateLog;
import com.ruoyi.common.utils.ObjectUtils;
import com.ruoyi.common.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author gy
 * @date 2023-06-13 11:25
 */
@RestController
@Api(tags = "流程画布")
@RequestMapping("/liangyuan/process_canvas")
public class ProcessCanvasController {

    @Autowired
    private ProcessCanvasService processCanvasService;

    /**
     * 查询流程画布
     * @param dto 流程画布查询条件
     * @return R
     */
    @GetMapping("/list")
    @OperateLog(title = "查询流程画布")
    @ApiOperation("查询流程画布")
    public R list(ProcessCanvasDto dto){
        List<ProcessCanvasEntity>  list = processCanvasService.list(new QueryWrapper<ProcessCanvasEntity>()
                .eq(dto.getId()!=null,"id",dto.getId())
                .eq(dto.getPlanId()!=null,"plan_id",dto.getPlanId())
                .eq(dto.getProjectId()!=null,"project_id",dto.getProjectId())
                .eq(dto.getFlowId()!=null,"flow_id",dto.getFlowId()));
        if (list.size()==1){
            return R.ok("查询成功",list.get(0));
        }else if (list.isEmpty()){
            return R.ok("查询成功",null);
        }{
            return R.ok("查询成功",list);
        }
    }

    /**
     * 新增流程画布
     * @param dto 新增流程画布dto
     * @return R
     */
    @PostMapping("/insert")
    @OperateLog(title = "新增流程画布")
    @ApiOperation("新增流程画布")
    public R insert(@RequestBody ProcessCanvasDto dto){
        ProcessCanvasEntity entity = ObjectUtils.transformObj(dto,ProcessCanvasEntity.class);
        return processCanvasService.save(entity) ? R.ok("新增流程画布成功") : R.error("新增流程画布失败");
    }

    /**
     * 新增或更新流程画布
     * @param dto 流程画布dto
     * @return R
     */
    @PostMapping("/saveorupdate")
    @OperateLog(title = "新增或更新流程画布")
    @ApiOperation("新增或更新流程画布")
    public R saveorupdate(@RequestBody ProcessCanvasDto dto){
        ProcessCanvasEntity entity = ObjectUtils.transformObj(dto,ProcessCanvasEntity.class);
        return processCanvasService.saveOrUpdate(entity, new QueryWrapper<ProcessCanvasEntity>()
                .eq(dto.getProjectId()!=null,"project_id",dto.getProjectId())
                .eq(dto.getFlowId()!=null,"flow_id",dto.getFlowId()))
                ? R.ok("新增或更新流程画布成功") : R.error("新增或更新流程画布失败");
    }

    /**
     * 更新流程画布
     * @param dto 更新流程画布dto
     * @return R
     */
    @PutMapping("/update")
    @OperateLog(title = "更新流程画布")
    @ApiOperation("更新流程画布")
    public R update(@RequestBody ProcessCanvasDto dto){
        ProcessCanvasEntity entity = ObjectUtils.transformObj(dto,ProcessCanvasEntity.class);
        return processCanvasService.updateById(entity) ? R.ok("修改流程画布成功") : R.error("修改流程画布失败");
    }

    /**
     * 删除流程画布
     * @param ids 需要删除的流程画布ids
     * @return R
     */
    @DeleteMapping("/delete")
    @OperateLog(title = "删除流程画布")
    @ApiOperation("删除流程画布")
    public R delete(Integer[] ids){
        for (Integer i:ids){
            processCanvasService.removeById(i);
        }
        return R.ok("删除流程画布成功");
    }
}
