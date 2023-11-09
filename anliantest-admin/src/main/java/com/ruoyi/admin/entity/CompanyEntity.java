package com.ruoyi.admin.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 企业信息表
 *
 * @author ZhuYiCheng
 * @date 2023/3/25 15:12
 */
@Data
@TableName("ly_company")
public class CompanyEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 自增主键ID
     */
    @TableId
    private Long id;

    /**
     * 统一社会信用代码
     */
    private String code;

    /**
     * 企业名称
     */
    private String company;

    /**
     * 成立日期
     */
    private Date registerDate;

    /**
     * 注册资本
     */
    private String capital;

    /**
     * 注册地址
     */
    private String address;

    /**
     * 法人代表
     */
    private String legalname;

    /**
     * 企业性质
     */
    private String companyProperty;

    /**
     * 经济类型
     */
    private String economicType;

    /**
     * 行业类别
     */
    private String industryCategory;


    /**
     * 企业规模
     */
    private String companyScale;

    /**
     * 主营产品
     */
    private String products;

    /**
     * 人员规模(人)
     */
    private String population;

    /**
     * 经营范围
     */
    private String scope;

    /**
     * 客户属性(0公有，1私有 )
     */
    private Integer state;

    /**
     * 省份
     */
    private String province;

    /**
     * 城市
     */
    private String city;

    /**
     * 区县
     */
    private String area;

    /**
     * 办公地址
     */
    private String officeAddress;

    /**
     * 联系人
     */
    private String contact;

    /**
     * 联系电话（手机）
     */
    private String mobile;

    /**
     * 固定电话
     */
    private String telephone;

    /**
     * 传真
     */
    private String fax;


    /**
     * 职业病危害风险分类(0一般、1较重、2严重)
     */
    private Integer riskLevel;


    /**
     * 产量信息
     */
    private String yields;

    /**
     * 客户类型（0潜在，1新，2老，3忠诚）
     */
    private Integer type;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 录入人Id
     */
    private Integer userId;

    /**
     * 录入人姓名
     */
    private String username;

    /**
     * 更新者
     */
    private String updateBy;

    /**
     * 合作状态(0潜在、1意向、2已合作)
     */
    private Integer contractStatus;

    /**
     * 客户归属人id
     */
    private Integer belongId;

    /**
     * 客户归属
     */
    private String belong;

    /**
     * 首次合作日期
     */
    private Date contractFirst;

    /**
     * 最近合作日期
     */
    private Date contractLast;

    /**
     * 最后跟进时间
     */
    private Date lastTrackTime;

    /**
     * 数据入库时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 数据所属公司
     */
    private String dataBelong;


}
