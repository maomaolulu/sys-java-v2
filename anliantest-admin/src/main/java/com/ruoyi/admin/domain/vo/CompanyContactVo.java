package com.ruoyi.admin.domain.vo;

import com.ruoyi.admin.entity.CompanyContactEntity;
import lombok.Data;

import java.util.List;

/**
 * @author ZhuYiCheng
 * @date 2023/3/30 9:52
 */
@Data
public class CompanyContactVo {

    private static final long serialVersionUID = 1L;

    /**
     * 联系人信息列表
     */
    private List<CompanyContactEntity> contactEntityList;
}
