package com.ruoyi.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.admin.domain.dto.ProjectDto;
import com.ruoyi.admin.domain.dto.ProjectInvoiceDto;
import com.ruoyi.admin.domain.dto.ProjectReceiptDto;
import com.ruoyi.admin.domain.vo.MoneyVo;
import com.ruoyi.admin.domain.vo.ProjectInvoiceVo;
import com.ruoyi.admin.domain.vo.ProjectReceiptVo;
import com.ruoyi.admin.entity.*;
import com.ruoyi.admin.mapper.ContractMapper;
import com.ruoyi.admin.mapper.ProjectMapper;
import com.ruoyi.admin.service.*;
import com.ruoyi.common.enums.DeleteFlag;
import com.ruoyi.common.enums.Numbers;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.pageUtil;
import com.ruoyi.system.common.CodeGenerateService;
import com.ruoyi.system.login.utils.ShiroUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author ZhuYiCheng
 * @date 2023/4/10 17:51
 */
@Service
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, ProjectEntity> implements ProjectService {

    @Autowired
    private ContractMapper contractMapper;

    @Autowired
    private ProjectMapper projectMapper;

    @Autowired
    private ProjectDateService projectDateService;

    @Autowired
    private ProjectAmountService projectAmountService;

    @Autowired
    private QuotationService quotationService;

    @Autowired
    private ProjectUserService projectUserService;

    @Autowired
    private ProjectRecordService projectRecordService;

    @Autowired
    private CodeGenerateService codeGenerateService;

    @Autowired
    private InvoiceService lyInvoiceService;

    @Autowired
    private ReceiptRecordService receiptRecordService;


    /**
     * 新增项目
     */
    @Override
    @Transactional
    public Boolean addProject(ProjectEntity projectEntity) {

        ContractEntity contract = contractMapper.selectById(projectEntity.getContractId());
        Date nowDate = DateUtils.getNowDate();

        projectEntity.setCompanyOrder(contract.getCompanyOrder());
        projectEntity.setBusinessSource(contract.getBusinessSource());
        projectEntity.setUserid(Math.toIntExact(ShiroUtils.getUserId()));
        projectEntity.setSalesmenid(ShiroUtils.getUserId());
        //放userName
        String userName = ShiroUtils.getUserName();
        projectEntity.setUsername(userName);
        projectEntity.setSalesmen(userName);
        projectEntity.setCreatetime(nowDate);
        projectEntity.setUpdatetime(nowDate);

        //存项目信息
        String projectCode = codeGenerateService.getProjectCode(projectEntity.getContractIdentifier());
        projectEntity.setIdentifier(projectCode);
        boolean b = this.save(projectEntity);

        //存过之后取出项目id,关联项目时间表(ly_project_date)，项目金额表(ly_project_amount),报价单基础表(ly_quotation)

        //存ProjectDateEntity
        ProjectDateEntity projectDateEntity = new ProjectDateEntity();
        projectDateEntity.setProjectId(projectEntity.getId());
        projectDateEntity.setEntrustDate(nowDate);
        boolean b1 = projectDateService.save(projectDateEntity);
        //存ProjectAmountEntity
        ProjectAmountEntity projectAmountEntity = new ProjectAmountEntity();
        projectAmountEntity.setProjectId(projectEntity.getId());
        projectAmountEntity.setContractId(projectEntity.getContractId());
        projectAmountEntity.setTotalMoney(projectEntity.getTotalMoney());
        projectAmountEntity.setCommission(projectEntity.getCommission());
        projectAmountEntity.setEvaluationFee(projectEntity.getEvaluationFee());
        projectAmountEntity.setSubprojectFee(projectEntity.getSubprojectFee());
        projectAmountEntity.setServiceCharge(projectEntity.getServiceCharge());
        projectAmountEntity.setOtherExpenses(projectEntity.getOtherExpenses());
        boolean b2 = projectAmountService.save(projectAmountEntity);
        //项目关联的报价单，存合同id，项目id
        QuotationEntity quotationEntity = new QuotationEntity();
        quotationEntity.setContractId(projectEntity.getContractId());
        quotationEntity.setId(projectEntity.getQuotationId());
        quotationEntity.setCode(projectEntity.getQuotationCode());
        quotationEntity.setProjectId(projectEntity.getId());
        boolean b3 = quotationService.updateById(quotationEntity);

        //修改合同表关联项目数
        contract.setProjectQuantity(contract.getProjectQuantity() + 1);
        //修改合同金额
        BigDecimal totalMoney = contract.getTotalMoney();
        if (totalMoney != null) {
            totalMoney = totalMoney.add(projectEntity.getTotalMoney());
        } else {
            BigDecimal totalMoney1 = new BigDecimal(0);
            totalMoney = totalMoney1.add(projectEntity.getTotalMoney());
        }
        contract.setTotalMoney(totalMoney);
        contractMapper.updateById(contract);

        return b && b1 && b2 && b3;
    }

    /**
     * 项目列表查询
     */
    @Override
    public List<ProjectEntity> getMyProjectList(ProjectDto projectDto) {
        Long salesmenid = projectDto.getSalesmenid();
        String identifier = projectDto.getIdentifier();
        String company = projectDto.getCompany();
        String startSignDate = projectDto.getStartSignDate();
        String endSignDate = projectDto.getEndSignDate();
        Integer status = projectDto.getStatus();
        Integer contractSignStatus = projectDto.getContractSignStatus();
        pageUtil.startPage();
        QueryWrapper<ProjectEntity> queryWrapper = new QueryWrapper<ProjectEntity>()
                .eq(!ShiroUtils.isAdmin() && salesmenid != null, "p.salesmenid", salesmenid)
                .like(StringUtils.isNotBlank(company), "p.company", company)
                .like(StringUtils.isNotBlank(identifier), "p.identifier", identifier)
                .ge(StringUtils.isNotBlank(startSignDate), "p.contract_sign_date", startSignDate)
                .le(StringUtils.isNotBlank(endSignDate), "p.contract_sign_date", endSignDate)
                .eq(status != null, "p.status", status)
                .eq(contractSignStatus != null, "p.contract_sign_status", contractSignStatus)
                .eq("p.del_flag", DeleteFlag.NO.ordinal())
                .orderByDesc("p.createtime");
        return projectMapper.getList(queryWrapper);
    }


    /**
     * 计算项目金额
     *
     * @param projectDto
     * @return
     */
    @Override
    public MoneyVo countMoney(ProjectDto projectDto) {
        Long salesmenid = projectDto.getSalesmenid();
        String identifier = projectDto.getIdentifier();
        String company = projectDto.getCompany();
        String startSignDate = projectDto.getStartSignDate();
        String endSignDate = projectDto.getEndSignDate();
        Integer status = projectDto.getStatus();
        Integer contractSignStatus = projectDto.getContractSignStatus();

        QueryWrapper<ProjectEntity> queryWrapper = new QueryWrapper<ProjectEntity>()
                .eq(!ShiroUtils.isAdmin() && salesmenid != null, "p.salesmenid", salesmenid)
                .like(StringUtils.isNotBlank(company), "p.company", company)
                .like(StringUtils.isNotBlank(identifier), "p.identifier", identifier)
                .ge(StringUtils.isNotBlank(startSignDate), "p.contract_sign_date", startSignDate)
                .le(StringUtils.isNotBlank(endSignDate), "p.contract_sign_date", endSignDate)
                .eq(status != null, "p.status", status)
                .eq(contractSignStatus != null, "p.contract_sign_status", contractSignStatus)
                .eq("p.del_flag", DeleteFlag.NO.ordinal());
        MoneyVo map = projectMapper.countMoney(queryWrapper);
        return map;
    }

    /**
     * 获取项目详情
     */
    @Override
    public ProjectEntity getDetail(Long id) {
        //项目信息
        ProjectEntity projectEntity = projectMapper.selectById(id);
        //项目金额信息
        ProjectAmountEntity projectAmountEntity = projectAmountService.getOne(new QueryWrapper<ProjectAmountEntity>().eq("project_id", id));
        if (projectAmountEntity != null) {
            projectEntity.setEvaluationFee(projectAmountEntity.getEvaluationFee());
            projectEntity.setCommission(projectAmountEntity.getCommission());
            projectEntity.setSubprojectFee(projectAmountEntity.getSubprojectFee());
            projectEntity.setServiceCharge(projectAmountEntity.getServiceCharge());
            projectEntity.setOtherExpenses(projectAmountEntity.getOtherExpenses());
        }
        return projectEntity;
    }


    /**
     * 终止项目
     *
     * @param projectEntity
     * @return
     */
    @Override
    public Boolean terminateProject(ProjectEntity projectEntity) {
        ProjectEntity project = projectMapper.selectById(projectEntity.getId());
        project.setStatus(projectEntity.getStatus());
        project.setTerminateReason(projectEntity.getTerminateReason());
        project.setTerminateTime(projectEntity.getTerminateTime());
        project.setUpdatetime(DateUtils.getNowDate());
        //终止项目
        boolean b = this.updateById(project);
        //终止项目关联的报价单
        QuotationEntity quotationEntity = quotationService.getOne(new QueryWrapper<QuotationEntity>()
                .eq("project_id", projectEntity.getId()));
        //修改报价单状态为丢单状态(报价单状态 1，审核中,2， 审核失败3， 报价中 4， 赢单5，丢单)
        quotationEntity.setStatus(5);
        boolean b1 = quotationService.updateById(quotationEntity);
        return b && b1;
    }


    /**
     * (逻辑)删除项目
     *
     * @param idList
     * @return
     */
    @Override
    public Boolean deleteProject(List<Long> idList) {
        //修改项目所属合同金额
        List<ProjectEntity> projectEntityList = projectMapper.selectBatchIds(idList);
        List<Long> contractIdList = new ArrayList<>();
        for (ProjectEntity projectEntity : projectEntityList) {
            ContractEntity contractEntity = contractMapper.selectById(projectEntity.getContractId());
            BigDecimal totalMoney = contractEntity.getTotalMoney();
            BigDecimal totalMoney1 = projectEntity.getTotalMoney();
            totalMoney = totalMoney.subtract(totalMoney1);
            contractEntity.setTotalMoney(totalMoney);
            contractMapper.updateById(contractEntity);
        }


        //逻辑删除项目关联报价单
        boolean b = quotationService.update(new UpdateWrapper<QuotationEntity>()
                .set("del_flag", DeleteFlag.YES.ordinal())
                .in("project_id", idList));
        //逻辑删除项目
        boolean b1 = this.update(new UpdateWrapper<ProjectEntity>()
                .set("del_flag", DeleteFlag.YES.ordinal())
                .in("id", idList));

        return b && b1;
    }

    /**
     * 项目下发
     *
     * @param projectEntity
     * @return
     */
    @Override
    @Transactional
    public ProjectEntity projectDistribute(ProjectEntity projectEntity) {
        Date date = new Date();
        //已启动
        projectEntity.setStatus(1);
        this.updateById(projectEntity);
        //项目时间信息
        ProjectDateEntity one = projectDateService.getOne(new LambdaQueryWrapper<ProjectDateEntity>().eq(ProjectDateEntity::getProjectId, projectEntity.getId()));
        if (one != null) {
            //下发日期
            one.setTaskReleaseDate(date);
            projectDateService.updateById(one);
        } else {
            ProjectDateEntity projectDateEntity = new ProjectDateEntity();

            //下发日期
            projectDateEntity.setTaskReleaseDate(date);
            projectDateService.save(projectDateEntity);

        }
        //记录
        ProjectRecord projectRecord = new ProjectRecord();
        projectRecord.setProjectId(projectEntity.getId());

        //类型 0：下发 1：现场调查
        projectRecord.setType(0);
        //小类型（颜色类型） 0：下发 1：现场调查
        projectRecord.setSubType(0);
        projectRecord.setTitle("下发");
        //修改人信息
        projectRecord.setCreateById(ShiroUtils.getUserId());
        projectRecord.setCreateBy(ShiroUtils.getUserName());
        projectRecord.setPlanDetails("下发日期为：" + DateUtil.format(date, "yyyy-MM-dd"));
        projectRecordService.save(projectRecord);


        //修改合同状态为履约中
        ContractEntity contractEntity1 = contractMapper.selectById(projectEntity.getContractId());
        if (contractEntity1.getPerformStatus() == 0) {
            ContractEntity contractEntity = new ContractEntity();
            contractEntity.setId(projectEntity.getContractId());
            contractEntity.setPerformStatus(1);
            contractMapper.updateById(contractEntity);
        }

        return projectEntity;
    }

    @Override
    public List<ProjectInvoiceVo> selectProjectAndInvoice(ProjectInvoiceDto projectInvoiceDto) {
        return getInvoiceList(projectInvoiceDto);
    }

    @Override
    public ProjectInvoiceVo selectProjectAndInvoiceById(Long id) {
        ProjectInvoiceDto dto = new ProjectInvoiceDto();
        dto.setId(id);
        List<ProjectInvoiceVo> list = getInvoiceList(dto);
        if (list.size() > 0) {
            ProjectInvoiceVo vo = list.get(0);
            vo.setInvoiceEntityList(lyInvoiceService.list(new QueryWrapper<InvoiceEntity>().eq("project_id", id).eq("delete_flag", DeleteFlag.NO.ordinal())));
            List<ReceiptRecordEntity> receiptlist = receiptRecordService.list(new QueryWrapper<ReceiptRecordEntity>().eq("project_id", id).eq("del_flag", DeleteFlag.NO.ordinal()));
            BigDecimal totalreceipt = BigDecimal.ZERO;
            if (receiptlist.size() > 0) {
                totalreceipt = receiptlist.stream().map(ReceiptRecordEntity::getReceiptMoney).reduce(BigDecimal.ZERO, BigDecimal::add);
            }
            vo.setInvoiceMoneyready(totalreceipt.subtract(vo.getInvoiceMoney()));
            return vo;
        } else {
            return null;
        }
    }

    private List<ProjectInvoiceVo> getInvoiceList(ProjectInvoiceDto projectInvoiceDto) {
        //开票比例小于1，按项目编号和企业名称模糊查询，按合同签订日期排序
        QueryWrapper<Object> queryWrapper = new QueryWrapper<>()
                .ne("p.`status`", Numbers.FOUR.ordinal())
                .eq("c.is_terminate", Numbers.ZERO.ordinal())
                .eq("c.review_status", Numbers.TWO.ordinal())
                .lt("(IFNULL(ir.invoice_money,0)/p.total_money)", Numbers.FIRST.ordinal())
                .eq(projectInvoiceDto.getId() != null, "p.id", projectInvoiceDto.getId())
                .like(StringUtils.isNotBlank(projectInvoiceDto.getIdentifier()), "p.identifier", projectInvoiceDto.getIdentifier())
                .like(StringUtils.isNotBlank(projectInvoiceDto.getCompany()), "p.company", projectInvoiceDto.getCompany())
                .orderByAsc("contract_sign_date");
        return baseMapper.selectProjectAndInvoice(queryWrapper);
    }

    @Override
    public void projectInvoiceDownload(HttpServletResponse response, List<ProjectInvoiceVo> list) throws IOException {
        //在内存操作，写到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);

        //自定义标题别名
        writer.addHeaderAlias("identifier", "项目编号");
        writer.addHeaderAlias("company", "合同隶属");
        writer.addHeaderAlias("businessSource", "业务来源");
        writer.addHeaderAlias("salesmen", "业务员");
        writer.addHeaderAlias("charge", "项目负责人");
        writer.addHeaderAlias("contractSignDate", "合同签订日期");
        writer.addHeaderAlias("totalMoney", "项目金额");
        writer.addHeaderAlias("invoiceMoney", "已开票");
        writer.addHeaderAlias("invoiceMoneyNotready", "未开票");
        writer.addHeaderAlias("invoiceMoneyRate", "开票进度");

        //默认配置
        writer.write(list, true);
        //设置content—type
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset:utf-8");

        //设置标题
        String fileName = URLEncoder.encode("项目开票信息", "UTF-8");
        //Content-disposition是MIME协议的扩展，MIME协议指示MIME用户代理如何显示附加的文件。
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
        ServletOutputStream outputStream = response.getOutputStream();

        //将Writer刷新到OutPut
        writer.flush(outputStream, true);
        outputStream.close();
        writer.close();
    }


    /**
     * 获取所有项目回款信息列表
     *
     * @param receiptDto
     * @return
     */
    @Override
    public List<ProjectReceiptVo> getProjectReceiptList(ProjectReceiptDto receiptDto) {
        //回款比例小于1，按项目编号和企业名称模糊查询，按合同签订日期排序
        QueryWrapper<Object> queryWrapper = new QueryWrapper<>()
                .eq("c.review_status", Numbers.TWO.ordinal())
                .eq("c.is_terminate", Numbers.ZERO.ordinal())
                .ne("p.status", Numbers.FOUR.ordinal())
                .lt("(IFNULL(lr.receipt_money,0)/p.total_money)", Numbers.FIRST.ordinal())
                .like(StringUtils.isNotBlank(receiptDto.getIdentifier()), "p.identifier", receiptDto.getIdentifier())
                .like(StringUtils.isNotBlank(receiptDto.getCompany()), "p.company", receiptDto.getCompany())
                .orderByAsc("p.contract_sign_date");

        List<ProjectReceiptVo> list = baseMapper.getProjectReceiptList(queryWrapper);
        return list;
    }


    /**
     * 根据id查询项目和回款信息
     *
     * @param receiptDto
     * @return
     */
    @Override
    public ProjectReceiptVo projectAndInvoiceDetail(ProjectReceiptDto receiptDto) {
        //回款比例小于1，按项目编号和企业名称模糊查询，按合同签订日期排序
        QueryWrapper<Object> queryWrapper = new QueryWrapper<>()
                .eq(receiptDto.getProjectId() != null, "p.id", receiptDto.getProjectId())
                .lt("(IFNULL(lr.receipt_money,0)/p.total_money) ", 1);
        ProjectReceiptVo receiptVo = baseMapper.projectAndInvoiceDetail(queryWrapper);

        if (receiptDto.getProjectId() != null && receiptVo != null) {
            //设置回款记录
            receiptVo.setReceiptEntityList(receiptRecordService.list(new QueryWrapper<ReceiptRecordEntity>()
                    .eq("project_id", receiptDto.getProjectId())
                    .eq("del_flag", DeleteFlag.NO.ordinal())));
            List<InvoiceEntity> invoiceEntityList = lyInvoiceService.list(new QueryWrapper<InvoiceEntity>()
                    .eq("project_id", receiptDto.getProjectId()));
            BigDecimal invoiceMoney = BigDecimal.ZERO;
            if (CollUtil.isNotEmpty(invoiceEntityList)) {
                for (InvoiceEntity invoiceEntity : invoiceEntityList) {
                    invoiceMoney = invoiceMoney.add(invoiceEntity.getInvoiceMoney());
                }
            }
            //设置已开票额
            receiptVo.setInvoiceMoney(invoiceMoney);
        }
        ArrayList<ProjectReceiptVo> list = new ArrayList<>();
        list.add(receiptVo);
        return receiptVo;
    }
}
