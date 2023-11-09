package com.ruoyi.admin.domain.vo;

import com.ruoyi.admin.entity.ProjectEntity;
import com.ruoyi.admin.entity.ContractEntity;
import com.ruoyi.admin.entity.QuotationEntity;
import lombok.Data;

import java.util.List;

/**
 * @author ZhuYiCheng
 * @date 2023/3/31 15:13
 */
@Data
public class ContractVo {

    /**
     * 合同基本信息
     */
    private ContractEntity contractEntity;

    /**
     * 项目信息列表
     */
    private List<ProjectEntity> projectEntityList;

    /**
     * 报价单信息列表
     */
    private List<QuotationEntity> quotationEntityList;

    /**
     * 0：创建 ，1：创建并提交审核
     */
    private Integer type;



}
