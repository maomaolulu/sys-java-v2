package com.ruoyi.admin.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zhanghao
 * @date 2023-5-24
 */
@Data
public class ContractOaVo implements Serializable {


    /**
     * 合同id
     */
    private Long id;

    /**
     * 0待评审,1评审中,2评审通过,3评审未通过,4撤销审核
     */
    private Integer reviewStatus;
}
