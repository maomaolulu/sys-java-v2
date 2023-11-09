package com.ruoyi.admin.domain.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * @author gy
 * @date 2023-06-21 14:45
 */
@Data
@Accessors(chain = true)
public class SubstanceTestLawDto {
    private static final long serialVersionUID = 1L;
    /**
     * ID
     */
    @TableId
    private Long id;
    /**
     * 检测标准(文号)
     */
    @NotBlank(message = "文号不可未空")
    private String testStandards;
    /**
     * 检测标准名称
     */
    @NotBlank(message = "名称不可未空")
    private String testStandardsName;
    /**
     * 颁布部门
     */
    private String issuingDepartment;
    /**
     * 法律效力
     */
    @NotBlank(message = "法律效力不可未空")
    private String legalEffect;
    /**
     * 状态(1现行，0废止,2发布)
     */
    private Integer status;
    /**
     * 开始实施日期
     */
    @JsonFormat(pattern="yyyy-MM-dd", timezone = "GMT+8")
    private Date implementationDate;
    /**
     * 废止日期
     */
    @JsonFormat(pattern="yyyy-MM-dd", timezone = "GMT+8")
    private Date abolitionDate;
    /**
     * 代替/修订关系
     */
    private String relationship;
    /**
     * 描述
     */
    private String describeInfo;

    /**检测类别
     * 1	公共场所
     * 2	公共场所集中空调通风系统
     * 3	人防工程
     * 4	水和废水
     * 5	生活饮用水
     * 6	一次性使用卫生用品
     * 7	消毒效果
     * 8	工作场所空气
     * 9	工作场所物理因素
     * 10	工作场所通风工程
     * 11	工作场所环境卫生条件
     * 12	环境空气和废气
     * 13	土壤
     * 14	固体废物
     * 15	噪声
     * 16	教室环境卫生
     * 17	学校厕所
     * 18	学生宿舍
     * 19	加油站油气回收
     * 20	室内空气
     * 21	固体废物
     * 22	水和废水（包括地下水）
     * 23	水（含大气降水）和废水
     * 24	水和废水（含大气降水）
     * 25	空气和废气
     * 26	土壤和沉积物
     * 27	振动
     * 28	洁净室（区）
     */
    private Integer testCategory;
    /**
     * 行业类别or地方
     */
    private String industryOrArea;
    /**
     * 标准类别(0检测标准,1法规标准)
     */
    private int standCategory;
}
