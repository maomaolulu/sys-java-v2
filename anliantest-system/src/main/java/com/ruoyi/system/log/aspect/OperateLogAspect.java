package com.ruoyi.system.log.aspect;

import com.alibaba.fastjson.JSON;
import com.ruoyi.common.annotation.OperateLog;
import com.ruoyi.common.enums.DeleteFlag;
import com.ruoyi.common.enums.OperateLogType;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.ip.IpUtils;
import com.ruoyi.common.utils.spring.SpringContextHolder;
import com.ruoyi.system.log.entity.SysOperateLog;
import com.ruoyi.system.log.event.SysOperateLogEvent;

import com.ruoyi.system.login.utils.ShiroUtils;
import com.ruoyi.system.user.entity.UserEntity;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import javax.servlet.ServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description
 * @Date 2023/4/25 13:48
 * @Author maoly
 **/
@Aspect
@Component
@Slf4j
public class OperateLogAspect {

    /**
     * @param joinPoint 切点
     */
    @Around("@annotation(operateLog)")
    @SneakyThrows
    public Object doAfterReturning(ProceedingJoinPoint joinPoint, OperateLog operateLog) {
        long beginTimeMillis = System.currentTimeMillis();
        SysOperateLog sysOperateLog = new SysOperateLog();
        Object result = null;
        UserEntity loginUser = ShiroUtils.getUserEntity();
        String userName = "";
        Long userId = null;
        if(loginUser != null){
            userName = loginUser.getUsername();
            userId = loginUser.getUserId();
        }
        String ip = IpUtils.getIpAddr(ServletUtils.getRequest());
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        String method = className + "." + methodName + "()";
        String title = operateLog.title();
        String params = "";
        if (operateLog.isSaveRequestData()) {
            Object[] args = joinPoint.getArgs();
            params = setRequestValue(args);
        }
        sysOperateLog.setOperateLogType(OperateLogType.NORMAL_LOG.getType());
        sysOperateLog.setTitle(title);
        sysOperateLog.setIp(ip);
        sysOperateLog.setParams(params);
        sysOperateLog.setMethod(method);
        sysOperateLog.setUserName(userName);
        sysOperateLog.setUserId(userId);
        sysOperateLog.setCreateBy(userName);
        sysOperateLog.setCreateTime(new Date());
        sysOperateLog.setUpdateBy(userName);
        sysOperateLog.setUpdateTime(new Date());
        sysOperateLog.setDeleteFlag(DeleteFlag.NO.ordinal());
        try {
            result = joinPoint.proceed();
        } catch (Exception e) {
            sysOperateLog.setOperateLogType(OperateLogType.EXCEPTION_LOG.getType());
            sysOperateLog.setExceptionMessage(e.getMessage());
            throw e;
        } finally {
            long endTimeMillis = System.currentTimeMillis();
            long processTime = endTimeMillis - beginTimeMillis;
            sysOperateLog.setProcessTime(processTime);
            SpringContextHolder.publishEvent(new SysOperateLogEvent(sysOperateLog));
        }
        return result;
    }
    /**
     *  获取请求的参数，放到log中
     * @throws Exception 异常
     */
    private String setRequestValue(Object[] args) {
        List<?> param = new ArrayList<>(Arrays.asList(args)).stream().filter(p -> !(p instanceof ServletResponse)).collect(Collectors.toList());
        String params = JSON.toJSONString(param, true);
//        String params = JSON.toJSONString(param, SerializerFeature.IgnoreErrorGetter);
        return params;
    }

}
