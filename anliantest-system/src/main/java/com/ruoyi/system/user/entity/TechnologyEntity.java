package com.ruoyi.system.user.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author ZhuYiCheng
 * @date 2023/4/28 13:43
 */
@Data
@TableName("t_person_technical_certificate")
public class TechnologyEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 自增id;主键）
     */
    @TableId
    private Long id;
    /**
     * 人员ID
     */
    private Long userId;
    /**
     * 人员名称
     */
    private String userName;
    /**
     * 技能名称
     */
    private String name;
    /**
     * 证书类型
     */
    private String type;
    /**
     * 证书编号
     */
    private String code;
    /**
     * 技能路径
     */
    private String path;
    /**
     * 有效期
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date expirationDate;
    /**
     * 是否永久有效(0，不是  1，是)
     */
    private Integer isForever;
    /**
     * 份数
     */
    private Integer numberCopies;
    /**
     * 页数
     */
    private Integer numberPages;
    /**
     * 文件收录日期
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date collectionDate;
    /**
     * 备注
     */
    private String remarks;
    /**
     * 文件收录时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    /**
     * 修改时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
}
