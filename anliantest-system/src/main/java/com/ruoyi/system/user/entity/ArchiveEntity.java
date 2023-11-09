package com.ruoyi.system.user.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.system.file.domain.SysAttachment;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author ZhuYiCheng
 * @date 2023/5/17 13:36
 */
@Data
@TableName(value = "sys_archive")
public class ArchiveEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键id
     */
    @TableId
    private Long id;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 档案类型(1人事档案，2技术档案)
     */
    private Integer type;
    /**
     * 档案名称
     */
    private String archiveName;
    /**
     * 证书编号
     */
    private String archiveCode;
    /**
     * 附件表id
     */
    private Long attachmentId;
    /**
     * 有效期
     */
    private String expirationDate;
    /**
     * 是否永久有效;0，不是  1，是)
     */
    private Integer isForever;
    /**
     * 父级id
     */
    private Long parentId;
    /**
     * 备注
     */
    private String remarks;
    /**
     * 逻辑删除;0有效1删除)
     */
    private Integer delFlag;
    /**
     * 创建人
     */
    private String createdBy;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createdTime;
    /**
     * 更新人
     */
    private String updatedBy;
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date updatedTime;
    /**
     * 档案对应附件列表
     */
    @TableField(exist = false)
    private List<SysAttachment> sysAttachmentList;
}
