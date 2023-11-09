//package com.ruoyi.admin;
//
//import com.ruoyi.system.common.CodeGenerateService;
//import net.sourceforge.groboutils.junit.v1.MultiThreadedTestRunner;
//import net.sourceforge.groboutils.junit.v1.TestRunnable;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
///**
// * @Description 高并发场景测试
// * @Date 2023/5/22 11:46
// * @Author maoly
// **/
//@SpringBootTest
//@RunWith(SpringRunner.class)
//public class HighConcurrency {
//
//    @Autowired
//    private CodeGenerateService codeGenerateService;
//
//    @Test
//    public void testConcurrentInitOrBind() {
//        TestRunnable runner = new TestRunnable() {
//            // 在runTest方法中填写自己的测试方法
//            @Override
//            public void runTest() throws Throwable {
//                //业务代码
//                String st = codeGenerateService.getQuotationCode();
//                System.out.println(st);
//
//               String st1 = codeGenerateService.getContractCode("采样检测");
//               System.out.println(st1);
//
//                String st2 = codeGenerateService.getContractCode("来样检测");
//                System.out.println(st2);
//
//                String st3 = codeGenerateService.getContractCode("评价");
//                System.out.println(st3);
//            }
//        };
//        // 一个数组，代表并发个数。此处并发5个
//        TestRunnable[] trs = new TestRunnable[5];
//        for (int i = 0; i < 5; i++) {
//            trs[i] = runner;
//        }
//        MultiThreadedTestRunner mttr = new MultiThreadedTestRunner(trs);
//        try {
//            mttr.runTestRunnables();
//        } catch (Throwable ex) {
//            ex.printStackTrace();
//        }
//    }
//}
