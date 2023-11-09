package com.ruoyi.system.schedule;

import com.ruoyi.system.common.CommonConstants;
import com.ruoyi.system.common.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @Description redis定时删除key
 * @Date 2023/5/19 8:55
 * @Author maoly
 **/
@Component
@Slf4j
public class CodeRedisDeleteJob {

    @Autowired
    private RedisService redisService;

    /**
     * 每天零点定时删除，每天23:59:59执行
     */
    @Scheduled(cron ="59 59 23 * * ?")
    public void taskEndDay(){
        if(redisService.hasKey(CommonConstants.QUOTATION_KEY)){
            boolean flag = redisService.deleteObject(CommonConstants.QUOTATION_KEY);
            if(flag){
                log.info("定时删除报价单自增编号成功");
            }else{
                log.info("定时删除报价单自增编号失败");
            }
        }

    }

    /**
     * 每个月最后一天的23:59:59执行
     */
    @Scheduled(cron = "59 59 23 L * ?")
    public void taskEndMonth(){
        if(redisService.hasKey(CommonConstants.CONTRACT_CY_KEY)){
            boolean flag = redisService.deleteObject(CommonConstants.CONTRACT_CY_KEY);
            if(flag){
                log.info("定时删除合同采样自增编号成功");
            }else{
                log.info("定时删除合同采样自增编号失败");
            }
        }
        if(redisService.hasKey(CommonConstants.CONTRACT_LY_KEY)){
            boolean flag = redisService.deleteObject(CommonConstants.CONTRACT_LY_KEY);
            if(flag){
                log.info("定时删除合同来样自增编号成功");
            }else{
                log.info("定时删除合同来样自增编号失败");
            }
        }
        if(redisService.hasKey(CommonConstants.CONTRACT_PJ_KEY)){
            boolean flag = redisService.deleteObject(CommonConstants.CONTRACT_PJ_KEY);
            if(flag){
                log.info("定时删除合同评价自增编号成功");
            }else{
                log.info("定时删除合同评价自增编号失败");
            }
        }
    }
}
