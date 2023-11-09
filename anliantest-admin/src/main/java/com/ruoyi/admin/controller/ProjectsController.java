package com.ruoyi.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.admin.domain.dto.ProjectDto;
import com.ruoyi.admin.domain.dto.ProjectInvoiceDto;
import com.ruoyi.admin.domain.dto.ProjectReceiptDto;
import com.ruoyi.admin.domain.vo.MoneyVo;
import com.ruoyi.admin.domain.vo.ProjectReceiptVo;
import com.ruoyi.admin.entity.ContractEntity;
import com.ruoyi.admin.entity.ProjectEntity;
import com.ruoyi.admin.service.ContractService;
import com.ruoyi.admin.service.ProjectService;
import com.ruoyi.common.annotation.OperateLog;
import com.ruoyi.common.utils.R;
import com.ruoyi.common.utils.pageUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author ZhuYiCheng
 * @date 2023/4/10 14:05
 */
@RestController
@Api(tags = "项目")
@RequestMapping("/liangyuan/project")
public class ProjectsController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ContractService contractService;


    /**
     * 新增项目
     */
    @PostMapping("/addProject")
    @OperateLog(title = "新增项目")
    @ApiOperation("新增项目")
//    @RequiresPermissions("liangyuan:contract:addProject")
    public R addProject(@RequestBody ProjectEntity projectEntity) {
        Boolean b = projectService.addProject(projectEntity);
        return b ? R.ok("新增项目成功") : R.error("新增项目失败");
    }

    /**
     * 项目列表查询
     */
    @GetMapping("/list")
    @ApiOperation("项目列表查询")
//    @RequiresPermissions("liangyuan:contract:list")
    public R getMyProjectList(ProjectDto projectDto) {
        List<ProjectEntity> projectEntityList = projectService.getMyProjectList(projectDto);
        MoneyVo moneyVo = projectService.countMoney(projectDto);
        return R.resultData(projectEntityList).put("map",moneyVo);
    }

    /**
     * 获取项目详情
     */
    @ApiOperation("获取项目详情")
    @GetMapping("/getDetail/{id}")
//    @RequiresPermissions("liangyuan:contract:getDetail")
    public R getDetail(@PathVariable("id") Long id) {
        ProjectEntity projectEntity = projectService.getDetail(id);
        return R.ok("查询成功", projectEntity);
    }

    /**
     * 终止项目
     */
    @PutMapping("/terminateProject")
    @OperateLog(title = "终止项目")
    @ApiOperation("终止项目")
//    @RequiresPermissions("liangyuan:contract:updateContract")
    public R terminateProject(@RequestBody ProjectEntity projectEntity) {
        Boolean b = projectService.terminateProject(projectEntity);
        return b ? R.ok("终止项目成功") : R.error("终止项目失败");
    }

    /**
     * (逻辑)删除项目
     *
     * @param idList
     * @return
     */
    @PutMapping("/deleteProject")
    @OperateLog(title = "删除项目")
    @ApiOperation("(逻辑)删除项目")
    public R deleteProject(@RequestBody List<Long> idList) {
        for (Long projectId : idList) {
            ProjectEntity projectEntity = projectService.getById(projectId);
            ContractEntity contractEntity = contractService.getOne(new QueryWrapper<ContractEntity>()
                    .eq("id", projectEntity.getContractId()));
            Integer reviewStatus = contractEntity.getReviewStatus();
            if(reviewStatus != 0){
                return R.error("项目"+projectId+"所属合同非[待评审状态]，删除失败");
            }
        }

        Boolean b = projectService.deleteProject(idList);
        return b ? R.ok("删除项目成功") : R.error("删除项目失败");
    }
    /**
     * 项目下发
     *
     * @param projectEntity
     * @return
     */
    @PostMapping("/projectDistribute")
    @OperateLog(title = "项目下发")
    @ApiOperation("项目下发")
    public R projectDistribute(@RequestBody ProjectEntity projectEntity) {


         projectService.projectDistribute(projectEntity);
        return R.ok("下发成功");
    }

    /**
     * 项目和开票信息
     * @param projectInvoiceDto
     * @return R
     */
    @GetMapping("/projectAndInvoice")
    @OperateLog(title = "项目和开票信息")
    @ApiOperation("项目和开票信息")
    public R projectAndInvoice(ProjectInvoiceDto projectInvoiceDto) {
        pageUtil.startPage();
        return R.resultData(projectService.selectProjectAndInvoice(projectInvoiceDto));
    }

    /**
     * 项目开票信息导出
     * @param projectInvoiceDto
     * @return R
     */
    @GetMapping("/projectInvoiceDownload")
    @OperateLog(title = "项目开票信息导出")
    @ApiOperation("项目开票信息导出")
    public void projectInvoiceDownload(HttpServletResponse response, ProjectInvoiceDto projectInvoiceDto) throws IOException {
        projectService.projectInvoiceDownload(response, projectService.selectProjectAndInvoice(projectInvoiceDto));
    }

    /**
     * 根据id查询项目和开票信息
     * @param id
     * @return R
     */
    @GetMapping("/projectAndInvoiceDetail")
    @OperateLog(title = "根据id查询项目和开票信息")
    @ApiOperation("根据id查询项目和开票信息")
    public R projectAndInvoiceDetail(Long id) {
        return R.ok("ok",projectService.selectProjectAndInvoiceById(id));
    }


    /**
     * 获取所有项目回款信息列表
     *
     * @return
     */
    @GetMapping("/getReceiptList")
    @ApiOperation("获取所有项目回款信息列表")
    public R getProjectReceiptList(ProjectReceiptDto receiptDto) {
        pageUtil.startPage();
        List<ProjectReceiptVo> projectReceiptVoList = projectService.getProjectReceiptList(receiptDto);

        return R.resultData(projectReceiptVoList);
    }

    /**
     * 根据id查询项目和回款信息
     * @param receiptDto
     * @return R
     */
    @GetMapping("/getReceiptDetail")
    @ApiOperation("根据id查询项目和回款信息")
    public R projectAndInvoiceDetail(ProjectReceiptDto receiptDto) {
        ProjectReceiptVo projectReceiptVo = projectService.projectAndInvoiceDetail(receiptDto);
        return R.ok("ok",projectReceiptVo);
    }



}
