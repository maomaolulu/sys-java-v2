package com.ruoyi.admin.domain.vo;

import com.ruoyi.admin.entity.CompanyAddressEntity;
import com.ruoyi.admin.entity.CompanyContactEntity;
import com.ruoyi.admin.entity.CompanyEntity;
import lombok.Data;


import java.util.List;

/**
 * @author ZhuYiCheng
 * @date 2023/3/28 10:41
 */
@Data
public class CompanyVo {
    private static final long serialVersionUID = 1L;

    /**
     * 客户基本信息
     */
    private CompanyEntity companyEntity;

    /**
     * 受检地址列表
     */
    private List<CompanyAddressEntity> addressEntityList;

    /**
     * 联系人信息列表
     */
    private List<CompanyContactEntity> contactEntityList;
}
