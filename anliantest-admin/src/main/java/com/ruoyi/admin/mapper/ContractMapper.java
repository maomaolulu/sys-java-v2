package com.ruoyi.admin.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ruoyi.admin.domain.vo.ProjectVo;
import com.ruoyi.admin.entity.ContractEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author ZhuYiCheng
 * @date 2023/3/31 15:25
 */
@Mapper
public interface ContractMapper extends BaseMapper<ContractEntity> {

    @Insert("INSERT INTO al_contract ( id, identifier, company_id, company, entrust_company_id, entrust_company, entrust_office_address, province, city, area, contact, telephone, entrust_type, type, review_status, company_order, business_source, project_quantity, salesmenid, salesmen, userid, username, createtime, updatetime )" +
            " VALUES (#{id}, #{identifier}, #{companyId}, #{company}, #{entrustCompanyId}, #{entrustCompany}, #{entrustOfficeAddress}, #{province}, #{city}, #{area}, #{contact}, #{telephone}, #{entrustType}, #{type}, #{reviewStatus}, #{companyOrder}, #{businessSource}, #{projectQuantity}, #{salesmenid}, #{salesmen}, #{userid}, #{username}, #{createtime}, #{updatetime} )")
    int add(ContractEntity contract);

    /**
     * 项目及报价信息  用于合同评审
     */
    @Select(" SELECT co.identifier,co.id contractId, pr.quotation_code,pr.type,pr.company,pr.office_address,qu.commission,\n" +
            "qu.preferential_fee,qu.subproject_fee,qu.evaluation_fee,qu.netvalue,qu.detect_fee,qu.report_fee,\n" +
            "qu.traffic_fee,SUM(qi.should_point) shouldPoint,SUM(qi.point)  point,  " +
            " GROUP_CONCAT(qi.substance) substances ,GROUP_CONCAT((case when  qi.is_subcontract = 0 then qi.substance else null end) )  isSubcontracts\n" +
            " from ly_project pr " +
            " left join ly_contract co on co.id=pr.contract_id " +
            "left join  ly_quotation qu on pr.id=qu.project_id\n" +
            "left join ly_quotation_info qi on qu.id =qi.quotation_id\n" +
            "where pr.contract_id = #{contractId  } \n" +
            "GROUP BY co.identifier,pr.quotation_code,pr.type,pr.company,pr.office_address,qu.commission,\n" +
            "qu.preferential_fee,qu.subproject_fee,qu.evaluation_fee,qu.netvalue,qu.detect_fee,qu.report_fee,\n" +
            "qu.traffic_fee,qi.should_point\n ")
    List<ProjectVo> proJectVoList(Long contractId);


    @Select("SELECT\n" +
            "\tc.id,\n" +
            "\tc.identifier,\n" +
            "\tc.company,\n" +
            "\tc.total_money,\n" +
            "\tc.project_quantity,\n" +
            "\tc.perform_status,\n" +
            "\tc.review_status,\n" +
            "\tc.contract_status,\n" +
            "\tc.sign_date,\n" +
            "\tc.createtime,\n" +
            "\tlr.receipt_money/c.total_money  AS receipt_money_rate,\n" +
            "\tir.invoice_money/c.total_money  AS invoice_money_rate\n" +
            "FROM\n" +
            "\t`ly_contract` c\n" +
            "\tLEFT JOIN ( SELECT SUM( IFNULL( receipt_money, 0 )) AS receipt_money, contract_id FROM `ly_receipt_record`  where del_flag = 0 or del_flag = null GROUP BY contract_id ) lr ON c.id = lr.contract_id\n" +
            "\tLEFT JOIN ( SELECT SUM( IFNULL( invoice_money, 0 )) AS invoice_money, contract_id FROM `ly_invoice_record`  where delete_flag = 0 or delete_flag = null GROUP BY contract_id ) ir ON c.id = ir.contract_id\n" +
            "${ew.customSqlSegment}")
    List<ContractEntity> getList(@Param(Constants.WRAPPER) QueryWrapper<ContractEntity> orderByDesc);
}
