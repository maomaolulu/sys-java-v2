package com.ruoyi.system.log.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.utils.pageUtil;
import com.ruoyi.system.log.dto.SysOperateLogPageRequestDTO;
import com.ruoyi.system.log.entity.SysOperateLog;
import com.ruoyi.system.log.mapper.SysOperateLogMapper;
import com.ruoyi.system.log.service.SysOperateLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description
 * @Date 2023/4/25 15:39
 * @Author maoly
 **/
@Service
public class SysOperateLogServiceImpl extends ServiceImpl<SysOperateLogMapper, SysOperateLog> implements SysOperateLogService {

    @Autowired
    private SysOperateLogMapper sysOperateLogMapper;

    @Override
    public List<SysOperateLog> queryPage(SysOperateLogPageRequestDTO sysOperateLogPageRequestDto) {
        QueryWrapper<SysOperateLog> queryWrapper = queryWrapperByParams(sysOperateLogPageRequestDto);
        pageUtil.startPage();
        return sysOperateLogMapper.selectList(queryWrapper);
    }

    private QueryWrapper<SysOperateLog> queryWrapperByParams(SysOperateLogPageRequestDTO sysOperateLogPageRequestDto) {
        QueryWrapper<SysOperateLog> queryWrapper = new QueryWrapper<SysOperateLog>()
                .like(StringUtils.checkValNotNull(sysOperateLogPageRequestDto.getTitle()),"title", sysOperateLogPageRequestDto.getTitle())
                .eq(sysOperateLogPageRequestDto.getUserId() != null,"user_id",sysOperateLogPageRequestDto.getUserId())
                .eq("delete_flag",sysOperateLogPageRequestDto.getDeleteFlag())
                .eq(StringUtils.isNotBlank(sysOperateLogPageRequestDto.getOperateLogType()),"operate_log_type",sysOperateLogPageRequestDto.getOperateLogType())
                .between(StringUtils.isNotBlank(sysOperateLogPageRequestDto.getStartTime())
                        && StringUtils.isNotBlank(sysOperateLogPageRequestDto.getEndTime()),
                        "create_time",sysOperateLogPageRequestDto.getStartTime(),sysOperateLogPageRequestDto.getEndTime())
                .orderByDesc("id");
        return queryWrapper;
    }

    @Override
    public int deleteByCreateTime(String preDate){
        QueryWrapper<SysOperateLog> deleteWrapper = new QueryWrapper<SysOperateLog>()
                .le(StringUtils.isNotBlank(preDate),"create_time",preDate);
        return sysOperateLogMapper.delete(deleteWrapper);
    }
}
