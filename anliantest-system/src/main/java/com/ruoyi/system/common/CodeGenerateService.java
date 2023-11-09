package com.ruoyi.system.common;

import com.ruoyi.common.constant.ContractTypeConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Description 编号生成工具类
 * @Date 2023/5/18 17:09
 * @Author maoly
 **/
@Service
public class CodeGenerateService {

    @Autowired
    private RedisService redisService;


    /**
     * 生成报价单编号
     *
     * @return
     */
    public String getQuotationCode() {
        synchronized (CodeGenerateService.class){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String nowDate = sdf.format(new Date());
            String result = "";
            if (redisService.hasKey(CommonConstants.QUOTATION_KEY)) {
                Integer redisLong = (Integer) redisService.get(CommonConstants.QUOTATION_KEY);
                String redisVal = String.valueOf(redisLong);
                if (redisVal.length() == CommonConstants.INT_ONE) {
                    result = nowDate + "00" + redisVal;
                } else if (redisVal.length() == CommonConstants.INT_TWO) {
                    result = nowDate + "0" + redisVal;
                } else {
                    result = nowDate + redisVal;
                }
            } else {
                redisService.increment(CommonConstants.QUOTATION_KEY, CommonConstants.REDIS_INCREMENT_DELTA);
                Integer redisLong = (Integer) redisService.get(CommonConstants.QUOTATION_KEY);
                String redisVal = String.valueOf(redisLong);
                if (redisVal.length() == CommonConstants.INT_ONE) {
                    result = nowDate + "00" + redisVal;
                }
            }
            redisService.increment(CommonConstants.QUOTATION_KEY, CommonConstants.REDIS_INCREMENT_DELTA);
            return result;
        }
    }


    /**
     * 生成合同单编号
     *
     * @return
     */
    public String getContractCode(String type) {
        synchronized (CodeGenerateService.class){
            SimpleDateFormat sdf = new SimpleDateFormat("yyMM");
            String nowDate = sdf.format(new Date());
            String result = "";
            //采样
            if (ContractTypeConstants.TYPE_CY.equals(type)) {
                //获取前缀
                String contractCodePrefix =ContractTypeConstants.RESULT_TYPE_CY;
                if (redisService.hasKey(CommonConstants.CONTRACT_CY_KEY)) {

                    String redisVal = String.valueOf(redisService.get(CommonConstants.CONTRACT_CY_KEY));

                    result = contractCodePrefix + nowDate + redisVal;
                } else {
                    redisService.set(CommonConstants.CONTRACT_CY_KEY, CommonConstants.REDIS_CONTRACT_INITIAL_VALUE);

                    String redisVal = String.valueOf(redisService.get(CommonConstants.CONTRACT_CY_KEY));

                    result = contractCodePrefix + nowDate + redisVal;
                }
                redisService.increment(CommonConstants.CONTRACT_CY_KEY, CommonConstants.REDIS_INCREMENT_DELTA);

            } else if (ContractTypeConstants.TYPE_LY.equals(type)) {
                //来样
                //获取前缀
                String contractCodePrefix =ContractTypeConstants.RESULT_TYPE_LY;
                if (redisService.hasKey(CommonConstants.CONTRACT_LY_KEY)) {

                    String redisVal = String.valueOf(redisService.get(CommonConstants.CONTRACT_LY_KEY));

                    result = contractCodePrefix + nowDate + redisVal;
                } else {
                    redisService.set(CommonConstants.CONTRACT_LY_KEY, CommonConstants.REDIS_CONTRACT_INITIAL_VALUE);

                    String redisVal = String.valueOf(redisService.get(CommonConstants.CONTRACT_LY_KEY));

                    result = contractCodePrefix + nowDate + redisVal;
                }
                redisService.increment(CommonConstants.CONTRACT_LY_KEY, CommonConstants.REDIS_INCREMENT_DELTA);
            } else {
                //评价
                //获取前缀
                String contractCodePrefix =ContractTypeConstants.RESULT_TYPE_PJ;
                if (redisService.hasKey(CommonConstants.CONTRACT_PJ_KEY)) {

                    String redisVal = String.valueOf(redisService.get(CommonConstants.CONTRACT_PJ_KEY));

                    result = contractCodePrefix + nowDate + redisVal;
                } else {
                    redisService.set(CommonConstants.CONTRACT_PJ_KEY, CommonConstants.REDIS_CONTRACT_INITIAL_VALUE);

                    String redisVal = String.valueOf(redisService.get(CommonConstants.CONTRACT_PJ_KEY));

                    result = contractCodePrefix + nowDate + redisVal;
                }
                redisService.increment(CommonConstants.CONTRACT_PJ_KEY, CommonConstants.REDIS_INCREMENT_DELTA);
            }
            return result;
        }
    }

    /**
     * 获取项目编号
     * @param contractIdentifier
     * @return
     */
    public String getProjectCode(String contractIdentifier) {
        synchronized (CodeGenerateService.class){
            String result = "";
            //项目所关联合同是否创建过项目
            if (redisService.hasKey(CommonConstants.CODE_KEY_PACKAGE+contractIdentifier)) {

                String redisVal = String.valueOf(redisService.get(CommonConstants.CODE_KEY_PACKAGE+contractIdentifier));
                if (redisVal.length() == CommonConstants.INT_ONE) {
                    result = contractIdentifier + "0" + redisVal;
                } else {
                    result = contractIdentifier + redisVal;
                }

            } else {
                //放入初始值1
                redisService.increment(CommonConstants.CODE_KEY_PACKAGE+contractIdentifier, CommonConstants.REDIS_INCREMENT_DELTA);

                String redisVal = String.valueOf(redisService.get(CommonConstants.CODE_KEY_PACKAGE+contractIdentifier));
                if (redisVal.length() == CommonConstants.INT_ONE) {
                    result = contractIdentifier + "0" + redisVal;
                }
            }
            redisService.increment(CommonConstants.CODE_KEY_PACKAGE+contractIdentifier, CommonConstants.REDIS_INCREMENT_DELTA);
            return result;
        }
    }


}
