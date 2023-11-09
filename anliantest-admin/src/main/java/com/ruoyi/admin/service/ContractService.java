package com.ruoyi.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.admin.domain.dto.ContractDto;
import com.ruoyi.admin.domain.vo.AssignmentVo;
import com.ruoyi.admin.domain.vo.ContractOaVo;
import com.ruoyi.admin.domain.vo.ContractVo;
import com.ruoyi.admin.entity.ContractEntity;
import com.ruoyi.common.utils.R;

import java.util.List;

/**
 * @author ZhuYiCheng
 * @date 2023/3/31 15:22
 */
public interface ContractService extends IService<ContractEntity> {

    /**
     * 新增合同
     */
    R addContract(ContractVo contractVo);
//
//    /**
//     * 创建并提交评审
//     */
//    R saveAndExamine(ContractVo contractVo);


    /**
     * 合同列表查询
     */
    List<ContractEntity> getContractList(ContractDto contractDto);

    /**
     * 获取合同详情
     */
    ContractVo getDetail(Long id);

    /**
     * 修改合同基本信息
     */
    Boolean updateContract(ContractEntity contractEntity);

    /**
     * 批量移交合同
     */
    Boolean transferContract(AssignmentVo assignmentVo);

    /**
     * 签订合同
     */
    Boolean signContract(ContractEntity contractEntity);

    /**
     * 终止合同
     */
    Boolean terminateContract(ContractEntity contractEntity);

    /**
     * 删除(逻辑)合同
     */
    Boolean deleteContract(List<Long> idList);


    /**
     * 合同评审
     */
    R contractExamine(Long id);


    /**
     * oa调用修改合同评审状态
     */
    R updateReviewStatus(ContractOaVo contractOaVo);


    /**
     * 合同撤回审批
     */
    R retract( Long id);

}
