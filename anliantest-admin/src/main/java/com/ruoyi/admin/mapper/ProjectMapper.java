package com.ruoyi.admin.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ruoyi.admin.domain.dto.ProjectPlanDto;
import com.ruoyi.admin.domain.vo.MoneyVo;
import com.ruoyi.admin.domain.vo.ProjectInvoiceVo;
import com.ruoyi.admin.domain.vo.ProjectReceiptVo;
import com.ruoyi.admin.entity.ProjectEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author ZhuYiCheng
 * @date 2023/3/31 15:27
 */
@Mapper
public interface ProjectMapper extends BaseMapper<ProjectEntity> {

    @Insert("INSERT INTO al_project ( id, identifier, contract_id, contract_identifier, company_id, company, province, city, area, office_address, type, detection_type, contact, telephone, project_name, salesmenid, salesmen, total_money, remarks, userid, username, createtime, updatetime, sample_type ) " +
            "VALUES ( #{id}, #{identifier}, #{contractId}, #{contractIdentifier}, #{companyId}, #{company}, #{province}, #{city}, #{area}, #{officeAddress}, #{type}, #{detectionType}, #{contact}, #{telephone}, #{projectName}, #{salesmenid}, #{salesmen}, #{totalMoney}, #{remarks}, #{userid}, #{username}, #{createtime}, #{updatetime}, #{sampleType} )")
    int add( ProjectEntity projectEntity);


    @Select(" SELECT pr.id, pr.identifier,pr.office_address,pr.company,pr.netvalue,pr.`status`,pr.small_status, " +
            " pr.charge_id,pr.charge,pd.plan_date,pd.plan_survey_date, " +
            " pd.task_release_date,pd.report_survey_date  FROM  ly_project pr \n" +
            "left join  `ly_project_date` pd on  pr.id=pd.project_id " +
            "${ew.customSqlSegment}  ")
    List<ProjectPlanDto> planPjList(@Param(Constants.WRAPPER) QueryWrapper wrapper);

    @Select(" SELECT \n" +
            "pr.id,pr.identifier,pr.type,pr.project_name,pr.company,pr.office_address,pr.contact,pr.telephone,pr.salesmenid,pr.small_status, " +
            "pr.salesmen,pr.`status`,  pr.total_money, pr.netvalue, pr.remarks, pr.charge_id, pr.charge,pd.plan_survey_date, " +
            "pd.task_release_date, pd.plan_date,pd.report_survey_date from ly_project pr " +
            "left join  `ly_project_date` pd on  pr.id=pd.project_id " +
            "where pr.id=#{id}  ")
    ProjectPlanDto info(Long id);

    @Select("SELECT p.id, p.contract_id, p.identifier,p.company,p.contract_identifier,p.company_order,p.business_source,p.project_name,p.salesmen,p.charge,p.contract_sign_date,p.total_money,\n" +
            "IFNULL(ir.invoice_money,0) invoice_money,\n" +
            "p.total_money-IFNULL(ir.invoice_money,0) invoice_money_notready,\n" +
            "(IFNULL(ir.invoice_money,0)/p.total_money) invoice_money_rate \n" +
            "FROM `ly_project` p \n" +
            "LEFT JOIN ( SELECT SUM( IFNULL( invoice_money, 0 )) AS invoice_money, project_id FROM `ly_invoice_record` WHERE delete_flag = 0 GROUP BY project_id ) ir ON p.id = ir.project_id \n" +
            "LEFT JOIN `ly_contract` c ON p.contract_id = c.id \n"+
            "${ew.customSqlSegment}")
    List<ProjectInvoiceVo> selectProjectAndInvoice(@Param(Constants.WRAPPER) QueryWrapper queryWrapper);

    void projectInvoiceDownload(HttpServletResponse response,List<ProjectInvoiceVo> list) throws IOException;

    ProjectInvoiceVo selectProjectAndInvoiceById(Long id);

    @Select("\t\tSELECT\n" +
            "\tp.id,\n" +
            "\tp.identifier,\n" +
            "\tp.contract_id,\n" +
            "\tp.contract_identifier,\n" +
            "\tp.company,\n" +
            "\tp.company_order,\n" +
            "\tp.business_source,\n" +
            "\tp.project_name,\n" +
            "\tp.salesmen,\n" +
            "\tp.charge,\n" +
            "\tp.contract_sign_date,\n" +
            "\tp.total_money,\n" +
            "\tIFNULL(lr.receipt_money,0),\n" +
            "\tp.total_money - IFNULL(lr.receipt_money,0) receipt_outstanding,\n" +
            "\tIFNULL(lr.receipt_money,0)/ p.total_money  receipt_money_rate \n" +
            "FROM\n" +
            "\t`ly_project` p\n" +
            "\tLEFT JOIN ( SELECT SUM( IFNULL( receipt_money, 0 )) AS receipt_money, project_id FROM `ly_receipt_record` \n" +
            "\tWHERE del_flag = 0 or del_flag = NULL\n" +
            "\tGROUP BY project_id ) lr ON p.id = lr.project_id\n" +
            "\tLEFT JOIN `ly_contract` c ON p.contract_id = c.id\n" +
            "${ew.customSqlSegment}")
    List<ProjectReceiptVo> getProjectReceiptList(@Param(Constants.WRAPPER) QueryWrapper queryWrapper);

    /**
     * 项目列表查询
     * @param queryWrapper
     * @return
     */
    @Select("SELECT\n" +
            "\tp.id,\n" +
            "\tp.identifier,\n" +
            "\tp.project_name,\n" +
            "\tp.company,\n" +
            "\tp.STATUS,\n" +
            "\tp.contract_sign_status,\n" +
            "\tp.contract_sign_date,\n" +
            "\tp.total_money,\n" +
            "\tp.createtime,\n" +
            "\tp.total_money - lr.receipt_money AS receipt_outstanding,\n" +
            "\tp.total_money - ir.invoice_money AS invoice_money_notready \n" +
            "FROM\n" +
            "\t`ly_project` p\n" +
            "\tLEFT JOIN ( SELECT SUM( IFNULL( receipt_money, 0 )) AS receipt_money, project_id FROM `ly_receipt_record` where del_flag = 0 or del_flag = null GROUP BY project_id ) lr ON p.id = lr.project_id\n" +
            "\tLEFT JOIN ( SELECT SUM( IFNULL( invoice_money, 0 )) AS invoice_money, project_id FROM `ly_invoice_record`  where delete_flag = 0 or delete_flag = null GROUP BY project_id ) ir ON p.id = ir.project_id\n" +
            "${ew.customSqlSegment}")
    List<ProjectEntity> getList(@Param(Constants.WRAPPER) QueryWrapper<ProjectEntity> queryWrapper);

    /**
     * 计算项目金额
     * @param queryWrapper
     * @return
     */
    @Select("SELECT\n" +
            "SUM(IFNULL(p.total_money,0)) as total_money, \n" +
            "SUM(IFNULL(lr.receipt_money,0)) as receipt_money,\n" +
            "SUM(IFNULL(p.total_money,0)) - SUM(IFNULL(lr.receipt_money,0)) as receipt_outstanding\n" +
            "  FROM\n" +
            "\t`ly_project`  p\n" +
            "\tLEFT JOIN ( SELECT SUM( IFNULL( receipt_money, 0 )) AS receipt_money, project_id FROM `ly_receipt_record` WHERE del_flag = 0 or del_flag = NULL GROUP BY project_id ) lr  ON p.id = lr.project_id \n" +
            "${ew.customSqlSegment}")
    MoneyVo countMoney(@Param(Constants.WRAPPER) QueryWrapper<ProjectEntity> queryWrapper);

    /**
     * 根据id查询项目和回款信息
     * @param queryWrapper
     * @return
     */
    @Select("\t\tSELECT\n" +
            "\tp.id,\n" +
            "\tp.identifier,\n" +
            "\tp.contract_id,\n" +
            "\tp.contract_identifier,\n" +
            "\tp.company,\n" +
            "\tp.company_order,\n" +
            "\tp.business_source,\n" +
            "\tp.project_name,\n" +
            "\tp.salesmen,\n" +
            "\tp.charge,\n" +
            "\tp.contract_sign_date,\n" +
            "\tp.total_money,\n" +
            "\tIFNULL(lr.receipt_money,0),\n" +
            "\tp.total_money - IFNULL(lr.receipt_money,0) receipt_outstanding,\n" +
            "\tIFNULL(lr.receipt_money,0)/ p.total_money  receipt_money_rate \n" +
            "FROM\n" +
            "\t`ly_project` p\n" +
            "\tLEFT JOIN ( SELECT SUM( IFNULL( receipt_money, 0 )) AS receipt_money, project_id FROM `ly_receipt_record` \n" +
            "\tWHERE del_flag = 0 or del_flag = NULL\n" +
            "\tGROUP BY project_id ) lr ON p.id = lr.project_id\n" +
            "${ew.customSqlSegment}")
    ProjectReceiptVo projectAndInvoiceDetail(@Param(Constants.WRAPPER) QueryWrapper<Object> queryWrapper);
}
