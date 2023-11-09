//package com.ruoyi.admin;
//
//import com.ruoyi.system.common.CodeGenerateService;
//import com.ruoyi.system.common.RedisService;
//import net.sourceforge.groboutils.junit.v1.MultiThreadedTestRunner;
//import net.sourceforge.groboutils.junit.v1.TestRunnable;
//import org.apache.commons.compress.utils.Lists;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
///**
// * @Description
// * @Date 2023/5/8 13:50
// * @Author maoly
// **/
//@SpringBootTest
//@RunWith(SpringRunner.class)
//public class RedisTest {
//
//    @Autowired
//    private RedisService redisService;
//
//    @Autowired
//    private RedisTemplate redisTemplate;
//
//    @Test
//    public void teRedis(){
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
//        String nowDate = sdf.format(new Date());
//        if(redisService.hasKey("quotation_code")){
//            Integer redisLong = (Integer) redisTemplate.opsForValue().get("quotation_code");
//            String redisString = String.valueOf(redisLong);
//            if(redisString.length() == 1){
//                System.out.println(nowDate + "00" + redisString);
//            } else if(redisString.length() == 2){
//                System.out.println(nowDate + "0" + redisString);
//            }else {
//                System.out.println(nowDate + redisString);
//            }
//        }
//        redisTemplate.opsForValue().increment("quotation_code",1);
//    }
//
//    @Test
//    public void testReids2(){
//        redisService.set("testkey",100);
//        redisService.increment("testkey",1);
//
//    }
//
//    @Test
//    public void testReids3(){
//        redisService.increment("testkey",1);
//
//    }
//
//
//}
