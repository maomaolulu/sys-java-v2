package com.ruoyi.system.log.dto;

import com.ruoyi.common.enums.DeleteFlag;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description 日志管理分页查询参数
 * @Date 2023/4/26 13:27
 * @Author maoly
 **/
@Data
public class SysOperateLogPageRequestDTO implements Serializable {

    private static final long serialVersionUID = -2954420808117037656L;

    /**
     * 标题
     */
    private String title;

    /**
     * 查询开始时间
     */
    private String startTime;

    /**
     * 查询结束时间
     */
    private String endTime;

    /**
     * 删除标识 0 未删除， 1已删除
     */
    private int deleteFlag = DeleteFlag.NO.ordinal();

    /**
     * 操作人id
     */
    private Long userId;

    /**
     * 日志类型
     */
    private String operateLogType;


}
