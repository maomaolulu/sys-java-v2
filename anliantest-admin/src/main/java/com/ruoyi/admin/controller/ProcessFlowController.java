package com.ruoyi.admin.controller;

import com.ruoyi.admin.domain.dto.ProcessFlowDto;
import com.ruoyi.admin.service.ProcessFlowService;
import com.ruoyi.common.annotation.OperateLog;
import com.ruoyi.common.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author gy
 * @date 2023-06-12 14:31
 */
@RestController
@Api(tags = "工艺流程")
@RequestMapping("/liangyuan/process_flow")
public class ProcessFlowController {

    @Autowired
    private ProcessFlowService processFlowService;

    /**
     * 查询工艺流程
     * @param processFlowDto 工艺流程查询条件(树形结构)
     * @return R
     */
    @GetMapping("/list")
    @OperateLog(title = "查询工艺流程")
    @ApiOperation("查询工艺流程")
    public R list(ProcessFlowDto processFlowDto){
        try {
            return R.ok("查询成功",processFlowService.selectWithPage(processFlowDto));
        }catch (Exception e){
            e.printStackTrace();
            return R.error(e.getMessage());
        }
    }

    /**
     * 新增工艺流程
     * @param processFlowDto 新增工艺流程实体
     * @return R
     */
    @PostMapping("/insert")
    @OperateLog(title = "新增工艺流程")
    @ApiOperation("新增工艺流程")
    public R insert(@RequestBody ProcessFlowDto processFlowDto){
        return processFlowService.saveAndModify(processFlowDto) ? R.ok("新增工艺流程成功") : R.error("新增工艺流程失败");
    }

    /**
     * 修改工艺流程
     * @param processFlowDto 修改工艺流程实体
     * @return R
     */
    @PutMapping("/update")
    @OperateLog(title = "修改工艺流程实体")
    @ApiOperation("修改工艺流程实体")
    public R update(@RequestBody ProcessFlowDto processFlowDto){
        return processFlowService.updateOneOrMore(processFlowDto) ? R.ok("修改工艺流程成功") : R.error("修改工艺流程失败");
    }

    /**
     * 删除工艺流程
     * @param id 删除工艺流程id
     * @return R
     */
    @DeleteMapping("/delete")
    @OperateLog(title = "删除工艺流程实体")
    @ApiOperation("删除工艺流程实体")
    public R delete(Long id){
        return processFlowService.deleteOnOrMore(id) ? R.ok("删除成功"):R.error("删除失败");
    }
}
