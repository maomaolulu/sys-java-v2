package com.ruoyi.admin.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ZhuYiCheng
 * @date 2023/3/25 16:32
 */
@Data
@TableName("ly_company_address")
public class CompanyAddressEntity implements Serializable {
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
     * 省份
     */
    private String province;

    /**
     * 市
     */
    private String city;

    /**
     * 区
     */
    private String area;

    /**
     * 受检地址
     */
    private String address;

    /**
     * 是否注册地址(0否，1是
     */
    private Integer isRegisteredAddress;

    /**
     * 备注
     */
    private String remark;

}
