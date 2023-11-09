package com.ruoyi.system.log.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description 系统操作日志
 * @Date 2023/4/25 14:13
 * @Author maoly
 **/
@TableName("sys_operate_log")
@Data
public class SysOperateLog implements Serializable {
    private static final long serialVersionUID = -1630762717794051719L;

    @TableId
    private Long id;

    /**
     * 操作项
     */
    private String title;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户邮箱
     */
    private String userName;

    /**
     * 请求方法
     */
    private String method;


    /**
     * 请求参数
     */
    private String params;

    /**
     *执行时长(毫秒)
     */
    private Long processTime;

    /**
     * IP地址
     */
    private String ip;

    /**
     * 日志类型
     */
    private String operateLogType;

    /**
     * 终端类型
     */
    private String clientType;

    /**
     * 异常信息
     */
    private String exceptionMessage;
//
//    /**
//     * 登录状态
//     */
//    private String loginType;

    /**
     * 创建者
     */
    private String createBy;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新者
     */
    private String updateBy;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 删除标识
     */
    private int deleteFlag;


}
