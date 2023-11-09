package com.ruoyi.admin.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author ZhuYiCheng
 * @date 2023/3/25 16:37
 */
@Data
@TableName("ly_company_contact")
public class CompanyContactEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 自增主键ID
     */
    @TableId
    private Long id;

    /**
     * 企业Id
     */
    private Long companyId;

    /**
     * 企业名称
     */
    private String company;

    /**
     * 联系人
     */
    private String contact;

    /**
     * 职务
     */
    private String position;

    /**
     * 联系方式
     */
    private String mobile;

    /**
     * 固定电话
     */
    private String telephone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 联系人类型
     */
    private String type;

    /**
     * 是否默认(0否，1是)
     */
    private Integer isDefault;

    /**
     * 备注
     */
    private String remark;
}
