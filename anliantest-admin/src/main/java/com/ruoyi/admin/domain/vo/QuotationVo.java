package com.ruoyi.admin.domain.vo;

import com.ruoyi.admin.entity.QuotationEntity;
import com.ruoyi.admin.entity.QuotationInfoEntity;
import com.ruoyi.admin.entity.WorkshopEntity;
import lombok.Data;

import java.util.List;

/**
 * @author ZhuYiCheng
 * @date 2023/4/20 10:51
 */
@Data
public class QuotationVo {

    /**
     * 报价单基本信息
     */
    private QuotationEntity quotationEntity;

    /**
     * 报价单详细信息列表
     */
    private List<QuotationInfoEntity> quotationInfoEntityList;

    /**
     * 车间岗位列表
     */
    private List<WorkshopEntity> workshopEntityList;
}
