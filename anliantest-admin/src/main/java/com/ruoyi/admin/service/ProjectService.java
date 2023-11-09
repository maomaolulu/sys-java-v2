package com.ruoyi.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.admin.domain.dto.ProjectDto;
import com.ruoyi.admin.domain.vo.MoneyVo;
import com.ruoyi.admin.domain.vo.ProjectInvoiceVo;
import com.ruoyi.admin.domain.dto.ProjectPlanDto;
import com.ruoyi.admin.domain.dto.ProjectReceiptDto;
import com.ruoyi.admin.domain.vo.ProjectReceiptVo;
import com.ruoyi.admin.domain.dto.ProjectInvoiceDto;
import com.ruoyi.admin.entity.ProjectEntity;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author ZhuYiCheng
 * @date 2023/4/10 17:50
 */
public interface ProjectService extends IService<ProjectEntity> {

    /**
     *新增项目
     */
    Boolean addProject(ProjectEntity projectEntity);

    /**
     *项目列表查询
     */
    List<ProjectEntity> getMyProjectList(ProjectDto projectDto);

    /**
     * 计算项目金额
     * @param projectDto
     * @return
     */
    MoneyVo countMoney(ProjectDto projectDto);

    /**
     * 获取项目详情
     */
    ProjectEntity getDetail(Long id);

    /**
     * 终止项目
     * @param projectEntity
     * @return
     */
    Boolean terminateProject(ProjectEntity projectEntity);

    /**
     * (逻辑)删除项目
     * @param idList
     * @return
     */
    Boolean deleteProject(List<Long> idList);

    /**
     * 项目下发
     * @param projectEntity
     * @return
     */
    ProjectEntity projectDistribute(ProjectEntity projectEntity);

    List<ProjectInvoiceVo> selectProjectAndInvoice(ProjectInvoiceDto projectInvoiceDto);

    void projectInvoiceDownload(HttpServletResponse response, List<ProjectInvoiceVo> list) throws IOException;

    ProjectInvoiceVo selectProjectAndInvoiceById(Long id);

    /**
     * 获取所有项目回款信息列表
     * @param receiptDto
     * @return
     */
    List<ProjectReceiptVo> getProjectReceiptList(ProjectReceiptDto receiptDto);

    /**
     * 根据id查询项目和回款信息
     * @param receiptDto
     * @return
     */
    ProjectReceiptVo projectAndInvoiceDetail(ProjectReceiptDto receiptDto);
}
