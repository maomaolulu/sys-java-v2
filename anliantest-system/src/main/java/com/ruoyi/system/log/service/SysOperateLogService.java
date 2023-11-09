package com.ruoyi.system.log.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.system.log.dto.SysOperateLogPageRequestDTO;
import com.ruoyi.system.log.entity.SysOperateLog;

import java.util.List;

public interface SysOperateLogService extends IService<SysOperateLog> {

    /**
     * 分页查询
     * @param queryDto
     * @return
     */
    List<SysOperateLog> queryPage(SysOperateLogPageRequestDTO queryDto);

    /**
     * 日志删除
     * @param preDate
     */
    int deleteByCreateTime(String preDate);
}
