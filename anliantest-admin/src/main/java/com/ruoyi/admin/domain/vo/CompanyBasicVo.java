package com.ruoyi.admin.domain.vo;

import com.ruoyi.admin.entity.CompanyAddressEntity;
import com.ruoyi.admin.entity.CompanyEntity;
import lombok.Data;

import java.util.List;

/**
 * @author ZhuYiCheng
 * @date 2023/3/29 13:45
 */
@Data
public class CompanyBasicVo {
    private static final long serialVersionUID = 1L;

    /**
     * 客户基本信息
     */
    private CompanyEntity companyEntity;

    /**
     * 受检地址列表
     */
    private List<CompanyAddressEntity> addressEntityList;
}
