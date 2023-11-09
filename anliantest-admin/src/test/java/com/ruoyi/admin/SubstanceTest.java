//package com.ruoyi.admin;
//
//import com.ruoyi.admin.entity.*;
//import com.ruoyi.admin.service.*;
//import com.ruoyi.common.enums.*;
//import com.ruoyi.common.utils.StringUtils;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.collections.CollectionUtils;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.util.*;
//import java.util.stream.Collectors;
//
///**
// * @Description
// * @Date 2023/6/6 9:19
// * @Author maoly
// **/
//@SpringBootTest
//@RunWith(SpringRunner.class)
//@Slf4j
//public class SubstanceTest {
//
//    @Autowired
//    private SubstanceInfoService substanceInfoService;
//
//    @Autowired
//    private SubstanceService substanceService;
//
//    @Autowired
//    private SubstanceStateLimitService substanceStateLimitService;
//
//    @Autowired
//    private SubstanceTestLawService substanceTestLawService;
//
//    @Autowired
//    private SubstanceTestMethodService substanceTestMethodService;
//
//    /**
//     * select * from substance_info where cas_code in ('25167-93-5','630-08-0','7440-50-8','7439-92-1','7439-98-7（Mo）','1317-65-3','14807-96-6','10101-41-4','7782-42-5','409-21-2','14808-60-7','12001-26-2','93763-70-3')
//     */
//
//    @Test
//    public void save(){
//        List<SubstanceEntity>  substanceEntityList = substanceService.getAllAndList();
//        //s_type=1 化学物质
//        handStype_1(substanceEntityList);
//        //s_type=2 粉尘
//        handStype_2(substanceEntityList);
//
//    }
//
//    /**
//     * 物理因素物质填充
//     */
//    @Test
//    public void saveWuli(){
//        //噪声
//        //handStype_3();
//        //高温作业
//        //handStype_4();
//        //紫外辐射
//        //handStype_5();
//        //微波辐射
//        //handStype_10();
//        //手传振动
//        //handStype_6();
//        //工频电场
//        //handStype_7();
//        //高频电磁场
//        //handStype_8();
//        //超高频辐射
//        //handStype_9();
//        //激光辐射
//        //handStype_11();
//    }
//
//
//
//
//    private void handStype_1(List<SubstanceEntity>  substanceEntityList){
//        //处理物质类型=1的数据,且cas编号不为空
//        List<SubstanceEntity>  substanceEntityList_1 = substanceEntityList.stream().filter(t -> t.getSType() == 1 && StringUtils.isNotBlank(t.getCasNo())).collect(Collectors.toList());
//        if(CollectionUtils.isNotEmpty(substanceEntityList_1)){
//            //通过cas号进行分组
//            Map<String, List<SubstanceEntity>> substanceEntityMap_1 = substanceEntityList_1.stream().collect(Collectors.groupingBy(SubstanceEntity::getCasNo));
//            //遍历
//            Iterator<Map.Entry<String, List<SubstanceEntity>>> iterator = substanceEntityMap_1.entrySet().iterator();
//            while (iterator.hasNext()){
//                Map.Entry<String, List<SubstanceEntity>> entry = iterator.next();
//                String casNo = entry.getKey();
//                log.info("处理化学的物质casno=[]",casNo);
//                List<SubstanceEntity> substanceEntityList1 = entry.getValue();
//                if(CollectionUtils.isNotEmpty(substanceEntityList1)){
//                    //只有一种物质
//                    if(substanceEntityList1.size() == 1){
//                        SubstanceEntity substanceEntity = substanceEntityList1.get(0);
//                        //物质信息
//                        Long substanceInfoId = saveSubstanceInfoEntity(substanceEntity,SubstanceType.POISON);
//                        //物质不同条件限值信息维护
//                        saveSubstanceStateLimitEntity(substanceEntity,substanceInfoId);
//                        //物质检测标准法律法规维护
//                        List<SubstanceDetectionEntity> substanceDetectionEntities = substanceEntity.getSubstanceDetectionEntities();
//                        if(CollectionUtils.isNotEmpty(substanceDetectionEntities)){
//                            List<SubstanceDetectionEntity> substanceDetectionEntyList = substanceDetectionEntities.stream().filter(t -> t.getLabSource().equals("嘉兴安联")).collect(Collectors.toList());
//                            Long substanceTestLawId = null;
//                            SubstanceDetectionEntity substanceDetectionEntity = new SubstanceDetectionEntity();
//                            if(CollectionUtils.isNotEmpty(substanceDetectionEntyList)){
//                                substanceDetectionEntity = substanceDetectionEntyList.get(0);
//                            }else{
//                                substanceDetectionEntity = substanceDetectionEntities.get(0);
//                            }
//                            substanceTestLawId = saveSubstanceTestLawEntity(substanceDetectionEntity,substanceInfoId);
//                            if(substanceTestLawId != null){
//                                //检测方法保存
//                                saveSubstanceTestMethodEntity(substanceDetectionEntity,substanceTestLawId);
//                            }
//                        }
//                    }
//                    //同时有多重物质
//                    if(substanceEntityList1.size() > 1){
//                        if(casNo.equals("25167-93-5")){
//                            SubstanceEntity substanceEntity = substanceEntityList1.get(0);
//                            //substanceEntity.setName("硝基氯苯");
//                            //物质信息
//                            Long substanceInfoId = saveSubstanceInfoEntity(substanceEntity,SubstanceType.POISON);
//                            substanceEntityList1.forEach(t -> {
//                                //物质不同条件限值信息维护
//                                saveSubstanceStateLimitEntity(t,substanceInfoId);
//                            });
//                            //物质检测标准法律法规维护
//                            List<SubstanceDetectionEntity> substanceDetectionEntities = substanceEntity.getSubstanceDetectionEntities();
//                            if(CollectionUtils.isNotEmpty(substanceDetectionEntities)){
//                                List<SubstanceDetectionEntity> substanceDetectionEntyList = substanceDetectionEntities.stream().filter(t -> t.getName().equals("硝基氯苯（气溶胶态）")).collect(Collectors.toList());
//                                Long substanceTestLawId = null;
//                                SubstanceDetectionEntity substanceDetectionEntity = new SubstanceDetectionEntity();
//                                if(CollectionUtils.isNotEmpty(substanceDetectionEntyList)){
//                                    substanceDetectionEntity = substanceDetectionEntyList.get(0);
//                                }else{
//                                    substanceDetectionEntity = substanceDetectionEntities.get(0);
//                                }
//                                substanceTestLawId = saveSubstanceTestLawEntity(substanceDetectionEntity,substanceInfoId);
//                                if(substanceTestLawId != null){
//                                    //检测方法保存
//                                    saveSubstanceTestMethodEntity(substanceDetectionEntity,substanceTestLawId);
//                                }
//                            }
//                        }
//                        if(casNo.equals("630-08-0")){
//                            SubstanceEntity substanceEntity = substanceEntityList1.get(0);
//                            //substanceEntity.setName("一氧化碳");
//                            //物质信息
//                            Long substanceInfoId = saveSubstanceInfoEntity(substanceEntity,SubstanceType.POISON);
//                            substanceEntityList1.forEach(t -> {
//                                //物质不同条件限值信息维护
//                                saveSubstanceStateLimitEntity(t,substanceInfoId);
//                            });
//                            //物质检测标准法律法规维护
//                            List<SubstanceDetectionEntity> substanceDetectionEntities = substanceEntity.getSubstanceDetectionEntities();
//                            if(CollectionUtils.isNotEmpty(substanceDetectionEntities)){
//                                List<SubstanceDetectionEntity> substanceDetectionEntyList = substanceDetectionEntities.stream().filter(t -> t.getLabSource().equals("嘉兴安联")).collect(Collectors.toList());
//                                Long substanceTestLawId = null;
//                                SubstanceDetectionEntity substanceDetectionEntity = new SubstanceDetectionEntity();
//                                if(CollectionUtils.isNotEmpty(substanceDetectionEntyList)){
//                                    substanceDetectionEntity = substanceDetectionEntyList.get(0);
//                                }else{
//                                    substanceDetectionEntity = substanceDetectionEntities.get(0);
//                                }
//                                substanceTestLawId = saveSubstanceTestLawEntity(substanceDetectionEntity,substanceInfoId);
//                                if(substanceTestLawId != null){
//                                    //检测方法保存
//                                    saveSubstanceTestMethodEntity(substanceDetectionEntity,substanceTestLawId);
//                                }
//                            }
//                        }
//                        if(casNo.equals("7440-50-8")){
//                            SubstanceEntity substanceEntity = substanceEntityList1.get(0);
//                            //substanceEntity.setName("铜");
//                            //物质信息
//                            Long substanceInfoId = saveSubstanceInfoEntity(substanceEntity,SubstanceType.POISON);
//                            substanceEntityList1.forEach(t -> {
//                                //物质不同条件限值信息维护
//                                saveSubstanceStateLimitEntity(t,substanceInfoId);
//                            });
//                            //物质检测标准法律法规维护
//                            List<SubstanceDetectionEntity> substanceDetectionEntities = substanceEntity.getSubstanceDetectionEntities();
//                            if(CollectionUtils.isNotEmpty(substanceDetectionEntities)){
//                                List<SubstanceDetectionEntity> substanceDetectionEntyList = substanceDetectionEntities.stream().filter(t -> t.getLabSource().equals("嘉兴安联")).collect(Collectors.toList());
//                                Long substanceTestLawId = null;
//                                SubstanceDetectionEntity substanceDetectionEntity = new SubstanceDetectionEntity();
//                                if(CollectionUtils.isNotEmpty(substanceDetectionEntyList)){
//                                    substanceDetectionEntity = substanceDetectionEntyList.get(0);
//                                }else{
//                                    substanceDetectionEntity = substanceDetectionEntities.get(0);
//                                }
//                                substanceTestLawId = saveSubstanceTestLawEntity(substanceDetectionEntity,substanceInfoId);
//                                if(substanceTestLawId != null){
//                                    //检测方法保存
//                                    saveSubstanceTestMethodEntity(substanceDetectionEntity,substanceTestLawId);
//                                }
//                            }
//                        }
//                        if(casNo.equals("7439-92-1")){
//                            SubstanceEntity substanceEntity = substanceEntityList1.get(0);
//                            //substanceEntity.setName("铅");
//                            //物质信息
//                            Long substanceInfoId = saveSubstanceInfoEntity(substanceEntity,SubstanceType.POISON);
//                            substanceEntityList1.forEach(t -> {
//                                //物质不同条件限值信息维护
//                                saveSubstanceStateLimitEntity(t,substanceInfoId);
//                            });
//                            //物质检测标准法律法规维护
//                            List<SubstanceDetectionEntity> substanceDetectionEntities = substanceEntity.getSubstanceDetectionEntities();
//                            if(CollectionUtils.isNotEmpty(substanceDetectionEntities)){
//                                List<SubstanceDetectionEntity> substanceDetectionEntyList = substanceDetectionEntities.stream().filter(t -> t.getLabSource().equals("杭州安联")).collect(Collectors.toList());
//                                Long substanceTestLawId = null;
//                                SubstanceDetectionEntity substanceDetectionEntity = new SubstanceDetectionEntity();
//                                if(CollectionUtils.isNotEmpty(substanceDetectionEntyList)){
//                                    substanceDetectionEntity = substanceDetectionEntyList.get(0);
//                                }else{
//                                    substanceDetectionEntity = substanceDetectionEntities.get(0);
//                                }
//                                substanceTestLawId = saveSubstanceTestLawEntity(substanceDetectionEntity,substanceInfoId);
//                                if(substanceTestLawId != null){
//                                    //检测方法保存
//                                    saveSubstanceTestMethodEntity(substanceDetectionEntity,substanceTestLawId);
//                                }
//                            }
//                        }
//                        if(casNo.equals("7439-98-7（Mo）")){
//                            SubstanceEntity substanceEntity = substanceEntityList1.get(0);
//                            //substanceEntity.setName("钼");
//                            //物质信息
//                            Long substanceInfoId = saveSubstanceInfoEntity(substanceEntity,SubstanceType.POISON);
//                            substanceEntityList1.forEach(t -> {
//                                //物质不同条件限值信息维护
//                                saveSubstanceStateLimitEntity(t,substanceInfoId);
//                            });
//                            //物质检测标准法律法规维护
//                            List<SubstanceDetectionEntity> substanceDetectionEntities = substanceEntity.getSubstanceDetectionEntities();
//                            if(CollectionUtils.isNotEmpty(substanceDetectionEntities)){
//                                List<SubstanceDetectionEntity> substanceDetectionEntyList = substanceDetectionEntities.stream().filter(t -> t.getLabSource().equals("杭州安联")).collect(Collectors.toList());
//                                Long substanceTestLawId = null;
//                                SubstanceDetectionEntity substanceDetectionEntity = new SubstanceDetectionEntity();
//                                if(CollectionUtils.isNotEmpty(substanceDetectionEntyList)){
//                                    substanceDetectionEntity = substanceDetectionEntyList.get(0);
//                                }else{
//                                    substanceDetectionEntity = substanceDetectionEntities.get(0);
//                                }
//                                substanceTestLawId = saveSubstanceTestLawEntity(substanceDetectionEntity,substanceInfoId);
//                                if(substanceTestLawId != null){
//                                    //检测方法保存
//                                    saveSubstanceTestMethodEntity(substanceDetectionEntity,substanceTestLawId);
//                                }
//                            }
//                        }
//
//                    }
//                }
//            }
//
//        }
//    }
//    private void handStype_2(List<SubstanceEntity> substanceEntityList) {
//        //处理物质类型=2的数据,且cas编号不为空
//        List<SubstanceEntity>  substanceEntityList_1 = substanceEntityList.stream().filter(t -> t.getSType() == 2 && StringUtils.isNotBlank(t.getCasNo())).collect(Collectors.toList());
//        if(CollectionUtils.isNotEmpty(substanceEntityList_1)){
//            //通过cas号进行分组
//            Map<String, List<SubstanceEntity>> substanceEntityMap_1 = substanceEntityList_1.stream().collect(Collectors.groupingBy(SubstanceEntity::getCasNo));
//            //遍历
//            Iterator<Map.Entry<String, List<SubstanceEntity>>> iterator = substanceEntityMap_1.entrySet().iterator();
//            while (iterator.hasNext()){
//                Map.Entry<String, List<SubstanceEntity>> entry = iterator.next();
//                String casNo = entry.getKey();
//                log.info("处理粉尘的物质casno=[]",casNo);
//                List<SubstanceEntity> substanceEntityList1 = entry.getValue();
//                if(CollectionUtils.isNotEmpty(substanceEntityList1)){
//                    //只有一种物质
//                    if(substanceEntityList1.size() == 1){
//                        SubstanceEntity substanceEntity = substanceEntityList1.get(0);
//                        //物质信息
//                        Long substanceInfoId = saveSubstanceInfoEntity(substanceEntity,SubstanceType.DUST);
//                        //物质不同条件限值信息维护
//                        saveSubstanceStateLimitEntity(substanceEntity,substanceInfoId);
//                        //物质检测标准法律法规维护
//                        List<SubstanceDetectionEntity> substanceDetectionEntities = substanceEntity.getSubstanceDetectionEntities();
//                        if(CollectionUtils.isNotEmpty(substanceDetectionEntities)){
//                            List<SubstanceDetectionEntity> substanceDetectionEntyList = substanceDetectionEntities.stream().filter(t -> t.getLabSource().equals("嘉兴安联")).collect(Collectors.toList());
//                            Long substanceTestLawId = null;
//                            SubstanceDetectionEntity substanceDetectionEntity = new SubstanceDetectionEntity();
//                            if(CollectionUtils.isNotEmpty(substanceDetectionEntyList)){
//                                substanceDetectionEntity = substanceDetectionEntyList.get(0);
//                            }else{
//                                substanceDetectionEntity = substanceDetectionEntities.get(0);
//                            }
//                            substanceTestLawId = saveSubstanceTestLawEntity(substanceDetectionEntity,substanceInfoId);
//                            if(substanceTestLawId != null){
//                                //检测方法保存
//                                saveSubstanceTestMethodEntity(substanceDetectionEntity,substanceTestLawId);
//                            }
//                        }
//                    }
//                    //同时有多重物质
//                    if(substanceEntityList1.size() > 1){
//                        if(casNo.equals("1317-65-3")){
//                            SubstanceEntity substanceEntity = substanceEntityList1.get(0);
//                            //substanceEntity.setName("大理石粉尘/石灰石粉尘");
//                            //物质信息
//                            Long substanceInfoId = saveSubstanceInfoEntity(substanceEntity,SubstanceType.DUST);
//                            substanceEntityList1.forEach(t -> {
//                                //物质不同条件限值信息维护
//                                saveSubstanceStateLimitEntity(t,substanceInfoId);
//                            });
//                            //物质检测标准法律法规维护
//                            List<SubstanceDetectionEntity> substanceDetectionEntities = substanceEntity.getSubstanceDetectionEntities();
//                            if(CollectionUtils.isNotEmpty(substanceDetectionEntities)){
//                                List<SubstanceDetectionEntity> substanceDetectionEntyList = substanceDetectionEntities.stream().filter(t -> t.getName().equals("嘉兴安联）")).collect(Collectors.toList());
//                                Long substanceTestLawId = null;
//                                SubstanceDetectionEntity substanceDetectionEntity = new SubstanceDetectionEntity();
//                                if(CollectionUtils.isNotEmpty(substanceDetectionEntyList)){
//                                    substanceDetectionEntity = substanceDetectionEntyList.get(0);
//                                }else{
//                                    substanceDetectionEntity = substanceDetectionEntities.get(0);
//                                }
//                                substanceTestLawId = saveSubstanceTestLawEntity(substanceDetectionEntity,substanceInfoId);
//                                if(substanceTestLawId != null){
//                                    //检测方法保存
//                                    saveSubstanceTestMethodEntity(substanceDetectionEntity,substanceTestLawId);
//                                }
//                            }
//                        }
//                        if(casNo.equals("14807-96-6")){
//                            SubstanceEntity substanceEntity = substanceEntityList1.get(0);
//                            //substanceEntity.setName("滑石粉尘");
//                            //物质信息
//                            Long substanceInfoId = saveSubstanceInfoEntity(substanceEntity,SubstanceType.DUST);
//                            substanceEntityList1.forEach(t -> {
//                                //物质不同条件限值信息维护
//                                saveSubstanceStateLimitEntity(t,substanceInfoId);
//                            });
//                            //物质检测标准法律法规维护
//                            List<SubstanceDetectionEntity> substanceDetectionEntities = substanceEntity.getSubstanceDetectionEntities();
//                            if(CollectionUtils.isNotEmpty(substanceDetectionEntities)){
//                                List<SubstanceDetectionEntity> substanceDetectionEntyList = substanceDetectionEntities.stream().filter(t -> t.getLabSource().equals("嘉兴安联")).collect(Collectors.toList());
//                                Long substanceTestLawId = null;
//                                SubstanceDetectionEntity substanceDetectionEntity = new SubstanceDetectionEntity();
//                                if(CollectionUtils.isNotEmpty(substanceDetectionEntyList)){
//                                    substanceDetectionEntity = substanceDetectionEntyList.get(0);
//                                }else{
//                                    substanceDetectionEntity = substanceDetectionEntities.get(0);
//                                }
//                                substanceTestLawId = saveSubstanceTestLawEntity(substanceDetectionEntity,substanceInfoId);
//                                if(substanceTestLawId != null){
//                                    //检测方法保存
//                                    saveSubstanceTestMethodEntity(substanceDetectionEntity,substanceTestLawId);
//                                }
//                            }
//                        }
//                        if(casNo.equals("10101-41-4")){
//                            SubstanceEntity substanceEntity = substanceEntityList1.get(0);
//                            //substanceEntity.setName("石膏粉尘");
//                            //物质信息
//                            Long substanceInfoId = saveSubstanceInfoEntity(substanceEntity,SubstanceType.DUST);
//                            substanceEntityList1.forEach(t -> {
//                                //物质不同条件限值信息维护
//                                saveSubstanceStateLimitEntity(t,substanceInfoId);
//                            });
//                            //物质检测标准法律法规维护
//                            List<SubstanceDetectionEntity> substanceDetectionEntities = substanceEntity.getSubstanceDetectionEntities();
//                            if(CollectionUtils.isNotEmpty(substanceDetectionEntities)){
//                                List<SubstanceDetectionEntity> substanceDetectionEntyList = substanceDetectionEntities.stream().filter(t -> t.getLabSource().equals("嘉兴安联")).collect(Collectors.toList());
//                                Long substanceTestLawId = null;
//                                SubstanceDetectionEntity substanceDetectionEntity = new SubstanceDetectionEntity();
//                                if(CollectionUtils.isNotEmpty(substanceDetectionEntyList)){
//                                    substanceDetectionEntity = substanceDetectionEntyList.get(0);
//                                }else{
//                                    substanceDetectionEntity = substanceDetectionEntities.get(0);
//                                }
//                                substanceTestLawId = saveSubstanceTestLawEntity(substanceDetectionEntity,substanceInfoId);
//                                if(substanceTestLawId != null){
//                                    //检测方法保存
//                                    saveSubstanceTestMethodEntity(substanceDetectionEntity,substanceTestLawId);
//                                }
//                            }
//                        }
//                        if(casNo.equals("7782-42-5")){
//                            SubstanceEntity substanceEntity = substanceEntityList1.get(0);
//                            //substanceEntity.setName("石墨粉尘");
//                            //物质信息
//                            Long substanceInfoId = saveSubstanceInfoEntity(substanceEntity,SubstanceType.DUST);
//                            substanceEntityList1.forEach(t -> {
//                                //物质不同条件限值信息维护
//                                saveSubstanceStateLimitEntity(t,substanceInfoId);
//                            });
//                            //物质检测标准法律法规维护
//                            List<SubstanceDetectionEntity> substanceDetectionEntities = substanceEntity.getSubstanceDetectionEntities();
//                            if(CollectionUtils.isNotEmpty(substanceDetectionEntities)){
//                                List<SubstanceDetectionEntity> substanceDetectionEntyList = substanceDetectionEntities.stream().filter(t -> t.getLabSource().equals("嘉兴安联")).collect(Collectors.toList());
//                                Long substanceTestLawId = null;
//                                SubstanceDetectionEntity substanceDetectionEntity = new SubstanceDetectionEntity();
//                                if(CollectionUtils.isNotEmpty(substanceDetectionEntyList)){
//                                    substanceDetectionEntity = substanceDetectionEntyList.get(0);
//                                }else{
//                                    substanceDetectionEntity = substanceDetectionEntities.get(0);
//                                }
//                                substanceTestLawId = saveSubstanceTestLawEntity(substanceDetectionEntity,substanceInfoId);
//                                if(substanceTestLawId != null){
//                                    //检测方法保存
//                                    saveSubstanceTestMethodEntity(substanceDetectionEntity,substanceTestLawId);
//                                }
//                            }
//                        }
//                        if(casNo.equals("409-21-2")){
//                            SubstanceEntity substanceEntity = substanceEntityList1.get(0);
//                            //substanceEntity.setName("碳化硅粉尘");
//                            //物质信息
//                            Long substanceInfoId = saveSubstanceInfoEntity(substanceEntity,SubstanceType.DUST);
//                            substanceEntityList1.forEach(t -> {
//                                //物质不同条件限值信息维护
//                                saveSubstanceStateLimitEntity(t,substanceInfoId);
//                            });
//                            //物质检测标准法律法规维护
//                            List<SubstanceDetectionEntity> substanceDetectionEntities = substanceEntity.getSubstanceDetectionEntities();
//                            if(CollectionUtils.isNotEmpty(substanceDetectionEntities)){
//                                List<SubstanceDetectionEntity> substanceDetectionEntyList = substanceDetectionEntities.stream().filter(t -> t.getLabSource().equals("嘉兴安联")).collect(Collectors.toList());
//                                Long substanceTestLawId = null;
//                                SubstanceDetectionEntity substanceDetectionEntity = new SubstanceDetectionEntity();
//                                if(CollectionUtils.isNotEmpty(substanceDetectionEntyList)){
//                                    substanceDetectionEntity = substanceDetectionEntyList.get(0);
//                                }else{
//                                    substanceDetectionEntity = substanceDetectionEntities.get(0);
//                                }
//                                substanceTestLawId = saveSubstanceTestLawEntity(substanceDetectionEntity,substanceInfoId);
//                                if(substanceTestLawId != null){
//                                    //检测方法保存
//                                    saveSubstanceTestMethodEntity(substanceDetectionEntity,substanceTestLawId);
//                                }
//                            }
//                        }
//                        if(casNo.equals("14808-60-7")){
//                            SubstanceEntity substanceEntity = substanceEntityList1.get(0);
//                            //substanceEntity.setName("矽尘");
//                            //物质信息
//                            Long substanceInfoId = saveSubstanceInfoEntity(substanceEntity,SubstanceType.DUST);
//                            substanceEntityList1.forEach(t -> {
//                                //物质不同条件限值信息维护
//                                saveSubstanceStateLimitEntity(t,substanceInfoId);
//                            });
//                            //物质检测标准法律法规维护
//                            List<SubstanceDetectionEntity> substanceDetectionEntities = substanceEntity.getSubstanceDetectionEntities();
//                            if(CollectionUtils.isNotEmpty(substanceDetectionEntities)){
//                                List<SubstanceDetectionEntity> substanceDetectionEntyList = substanceDetectionEntities.stream().filter(t -> t.getLabSource().equals("嘉兴安联")).collect(Collectors.toList());
//                                Long substanceTestLawId = null;
//                                SubstanceDetectionEntity substanceDetectionEntity = new SubstanceDetectionEntity();
//                                if(CollectionUtils.isNotEmpty(substanceDetectionEntyList)){
//                                    substanceDetectionEntity = substanceDetectionEntyList.get(0);
//                                }else{
//                                    substanceDetectionEntity = substanceDetectionEntities.get(0);
//                                }
//                                substanceTestLawId = saveSubstanceTestLawEntity(substanceDetectionEntity,substanceInfoId);
//                                if(substanceTestLawId != null){
//                                    //检测方法保存
//                                    saveSubstanceTestMethodEntity(substanceDetectionEntity,substanceTestLawId);
//                                }
//                            }
//                        }
//                        if(casNo.equals("12001-26-2")){
//                            SubstanceEntity substanceEntity = substanceEntityList1.get(0);
//                            //substanceEntity.setName("云母粉尘");
//                            //物质信息
//                            Long substanceInfoId = saveSubstanceInfoEntity(substanceEntity,SubstanceType.DUST);
//                            substanceEntityList1.forEach(t -> {
//                                //物质不同条件限值信息维护
//                                saveSubstanceStateLimitEntity(t,substanceInfoId);
//                            });
//                            //物质检测标准法律法规维护
//                            List<SubstanceDetectionEntity> substanceDetectionEntities = substanceEntity.getSubstanceDetectionEntities();
//                            if(CollectionUtils.isNotEmpty(substanceDetectionEntities)){
//                                List<SubstanceDetectionEntity> substanceDetectionEntyList = substanceDetectionEntities.stream().filter(t -> t.getLabSource().equals("嘉兴安联")).collect(Collectors.toList());
//                                Long substanceTestLawId = null;
//                                SubstanceDetectionEntity substanceDetectionEntity = new SubstanceDetectionEntity();
//                                if(CollectionUtils.isNotEmpty(substanceDetectionEntyList)){
//                                    substanceDetectionEntity = substanceDetectionEntyList.get(0);
//                                }else{
//                                    substanceDetectionEntity = substanceDetectionEntities.get(0);
//                                }
//                                substanceTestLawId = saveSubstanceTestLawEntity(substanceDetectionEntity,substanceInfoId);
//                                if(substanceTestLawId != null){
//                                    //检测方法保存
//                                    saveSubstanceTestMethodEntity(substanceDetectionEntity,substanceTestLawId);
//                                }
//                            }
//                        }
//                        if(casNo.equals("93763-70-3")){
//                            SubstanceEntity substanceEntity = substanceEntityList1.get(0);
//                            //substanceEntity.setName("珍珠岩粉尘");
//                            //物质信息
//                            Long substanceInfoId = saveSubstanceInfoEntity(substanceEntity,SubstanceType.DUST);
//                            substanceEntityList1.forEach(t -> {
//                                //物质不同条件限值信息维护
//                                saveSubstanceStateLimitEntity(t,substanceInfoId);
//                            });
//                            //物质检测标准法律法规维护
//                            List<SubstanceDetectionEntity> substanceDetectionEntities = substanceEntity.getSubstanceDetectionEntities();
//                            if(CollectionUtils.isNotEmpty(substanceDetectionEntities)){
//                                List<SubstanceDetectionEntity> substanceDetectionEntyList = substanceDetectionEntities.stream().filter(t -> t.getLabSource().equals("嘉兴安联")).collect(Collectors.toList());
//                                Long substanceTestLawId = null;
//                                SubstanceDetectionEntity substanceDetectionEntity = new SubstanceDetectionEntity();
//                                if(CollectionUtils.isNotEmpty(substanceDetectionEntyList)){
//                                    substanceDetectionEntity = substanceDetectionEntyList.get(0);
//                                }else{
//                                    substanceDetectionEntity = substanceDetectionEntities.get(0);
//                                }
//                                substanceTestLawId = saveSubstanceTestLawEntity(substanceDetectionEntity,substanceInfoId);
//                                if(substanceTestLawId != null){
//                                    //检测方法保存
//                                    saveSubstanceTestMethodEntity(substanceDetectionEntity,substanceTestLawId);
//                                }
//                            }
//                        }
//
//                    }
//                }
//            }
//
//        }
//    }
//    private void handStype_3() {
//        //物质信息
//        Long substanceInfoId = saveSubstanceInfoEntityWuli("噪声",SubstanceType.NOISE.ordinal());
//        //物质限值
//        List<SubstanceStateLimitEntity> substanceStateLimitEntityList = new ArrayList<>();
//        SubstanceStateLimitEntity substanceStateLimitEntity = new SubstanceStateLimitEntity();
//        substanceStateLimitEntity.setSubstanceInfoId(substanceInfoId);
//        substanceStateLimitEntity.setConditionNote("噪声");
//        substanceStateLimitEntity.setNoiseTimeFrequency("5d/w,=8h/d");
//        substanceStateLimitEntity.setNoisePeakValue(85);
//        substanceStateLimitEntity.setRemark("非稳态噪声计算8h等效声级");
//        substanceStateLimitEntity.setCreateBy("");
//        substanceStateLimitEntity.setUpdateBy("");
//        substanceStateLimitEntity.setCreateTime(new Date());
//        substanceStateLimitEntity.setUpdateTime(new Date());
//        substanceStateLimitEntity.setDeleteFlag(DeleteFlag.NO.ordinal());
//
//        SubstanceStateLimitEntity substanceStateLimitEntity1 = new SubstanceStateLimitEntity();
//        substanceStateLimitEntity1.setSubstanceInfoId(substanceInfoId);
//        substanceStateLimitEntity1.setConditionNote("噪声");
//        substanceStateLimitEntity1.setNoiseTimeFrequency("5d/w,≠8h/d");
//        substanceStateLimitEntity1.setNoisePeakValue(85);
//        substanceStateLimitEntity1.setRemark("计算8h等效声级");
//        substanceStateLimitEntity1.setCreateBy("");
//        substanceStateLimitEntity1.setUpdateBy("");
//        substanceStateLimitEntity1.setCreateTime(new Date());
//        substanceStateLimitEntity1.setUpdateTime(new Date());
//        substanceStateLimitEntity1.setDeleteFlag(DeleteFlag.NO.ordinal());
//
//        SubstanceStateLimitEntity substanceStateLimitEntity2 = new SubstanceStateLimitEntity();
//        substanceStateLimitEntity2.setSubstanceInfoId(substanceInfoId);
//        substanceStateLimitEntity2.setConditionNote("噪声");
//        substanceStateLimitEntity2.setNoiseTimeFrequency("≠5d/w");
//        substanceStateLimitEntity2.setNoisePeakValue(85);
//        substanceStateLimitEntity2.setRemark("计算40h等效声级");
//        substanceStateLimitEntity2.setCreateBy("");
//        substanceStateLimitEntity2.setUpdateBy("");
//        substanceStateLimitEntity2.setCreateTime(new Date());
//        substanceStateLimitEntity2.setUpdateTime(new Date());
//        substanceStateLimitEntity2.setDeleteFlag(DeleteFlag.NO.ordinal());
//
//        SubstanceStateLimitEntity substanceStateLimitEntity3 = new SubstanceStateLimitEntity();
//        substanceStateLimitEntity3.setSubstanceInfoId(substanceInfoId);
//        substanceStateLimitEntity3.setConditionNote("脉冲噪声");
//        substanceStateLimitEntity3.setNoiseTimeFrequency("n≤100");
//        substanceStateLimitEntity3.setNoisePeakValue(140);
//        substanceStateLimitEntity3.setRemark("");
//        substanceStateLimitEntity3.setCreateBy("");
//        substanceStateLimitEntity3.setUpdateBy("");
//        substanceStateLimitEntity3.setCreateTime(new Date());
//        substanceStateLimitEntity3.setUpdateTime(new Date());
//        substanceStateLimitEntity3.setDeleteFlag(DeleteFlag.NO.ordinal());
//
//        SubstanceStateLimitEntity substanceStateLimitEntity4 = new SubstanceStateLimitEntity();
//        substanceStateLimitEntity4.setSubstanceInfoId(substanceInfoId);
//        substanceStateLimitEntity4.setConditionNote("脉冲噪声");
//        substanceStateLimitEntity4.setNoiseTimeFrequency("100<n≤1000");
//        substanceStateLimitEntity4.setNoisePeakValue(130);
//        substanceStateLimitEntity4.setRemark("");
//        substanceStateLimitEntity4.setCreateBy("");
//        substanceStateLimitEntity4.setUpdateBy("");
//        substanceStateLimitEntity4.setCreateTime(new Date());
//        substanceStateLimitEntity4.setUpdateTime(new Date());
//        substanceStateLimitEntity4.setDeleteFlag(DeleteFlag.NO.ordinal());
//
//        SubstanceStateLimitEntity substanceStateLimitEntity5 = new SubstanceStateLimitEntity();
//        substanceStateLimitEntity5.setSubstanceInfoId(substanceInfoId);
//        substanceStateLimitEntity5.setConditionNote("脉冲噪声");
//        substanceStateLimitEntity5.setNoiseTimeFrequency("1000<n≤10000");
//        substanceStateLimitEntity5.setNoisePeakValue(120);
//        substanceStateLimitEntity5.setRemark("");
//        substanceStateLimitEntity5.setCreateBy("");
//        substanceStateLimitEntity5.setUpdateBy("");
//        substanceStateLimitEntity5.setCreateTime(new Date());
//        substanceStateLimitEntity5.setUpdateTime(new Date());
//        substanceStateLimitEntity5.setDeleteFlag(DeleteFlag.NO.ordinal());
//
//        substanceStateLimitEntityList.add(substanceStateLimitEntity);
//        substanceStateLimitEntityList.add(substanceStateLimitEntity1);
//        substanceStateLimitEntityList.add(substanceStateLimitEntity2);
//        substanceStateLimitEntityList.add(substanceStateLimitEntity3);
//        substanceStateLimitEntityList.add(substanceStateLimitEntity4);
//        substanceStateLimitEntityList.add(substanceStateLimitEntity5);
//        substanceStateLimitService.saveBatch(substanceStateLimitEntityList);
//
//        //法律法规
//        saveSubstanceTestLawEntityWuli(substanceInfoId,"GBZ/T 189.8","工作场所物理因素测量 噪声",TestCategory.CATEGORY_15.getCode());
//
//    }
//    private void handStype_4() {
//        //物质信息
//        Long substanceInfoId = saveSubstanceInfoEntityWuli("高温",SubstanceType.HIGH_TEMPERATURE.ordinal());
//        //物质限值
//        List<SubstanceStateLimitEntity> substanceStateLimitEntityList = new ArrayList<>();
//        SubstanceStateLimitEntity substanceStateLimitEntity1 = new SubstanceStateLimitEntity();
//        substanceStateLimitEntity1.setSubstanceInfoId(substanceInfoId);
//        substanceStateLimitEntity1.setConditionNote("高温");
//        substanceStateLimitEntity1.setContactTimeRate(100);
//        substanceStateLimitEntity1.setLaborIntensityOne(30);
//        substanceStateLimitEntity1.setLaborIntensityTwo(28);
//        substanceStateLimitEntity1.setLaborIntensityThree(26);
//        substanceStateLimitEntity1.setLaborIntensityFour(25);
//        substanceStateLimitEntity1.setRemark("");
//        substanceStateLimitEntity1.setCreateBy("");
//        substanceStateLimitEntity1.setUpdateBy("");
//        substanceStateLimitEntity1.setCreateTime(new Date());
//        substanceStateLimitEntity1.setUpdateTime(new Date());
//        substanceStateLimitEntity1.setDeleteFlag(DeleteFlag.NO.ordinal());
//
//        SubstanceStateLimitEntity substanceStateLimitEntity2 = new SubstanceStateLimitEntity();
//        substanceStateLimitEntity2.setSubstanceInfoId(substanceInfoId);
//        substanceStateLimitEntity2.setConditionNote("高温");
//        substanceStateLimitEntity2.setContactTimeRate(75);
//        substanceStateLimitEntity2.setLaborIntensityOne(31);
//        substanceStateLimitEntity2.setLaborIntensityTwo(29);
//        substanceStateLimitEntity2.setLaborIntensityThree(28);
//        substanceStateLimitEntity2.setLaborIntensityFour(26);
//        substanceStateLimitEntity2.setRemark("");
//        substanceStateLimitEntity2.setCreateBy("");
//        substanceStateLimitEntity2.setUpdateBy("");
//        substanceStateLimitEntity2.setCreateTime(new Date());
//        substanceStateLimitEntity2.setUpdateTime(new Date());
//        substanceStateLimitEntity2.setDeleteFlag(DeleteFlag.NO.ordinal());
//
//        SubstanceStateLimitEntity substanceStateLimitEntity3 = new SubstanceStateLimitEntity();
//        substanceStateLimitEntity3.setSubstanceInfoId(substanceInfoId);
//        substanceStateLimitEntity3.setConditionNote("高温");
//        substanceStateLimitEntity3.setContactTimeRate(50);
//        substanceStateLimitEntity3.setLaborIntensityOne(32);
//        substanceStateLimitEntity3.setLaborIntensityTwo(30);
//        substanceStateLimitEntity3.setLaborIntensityThree(29);
//        substanceStateLimitEntity3.setLaborIntensityFour(28);
//        substanceStateLimitEntity3.setRemark("");
//        substanceStateLimitEntity3.setCreateBy("");
//        substanceStateLimitEntity3.setUpdateBy("");
//        substanceStateLimitEntity3.setCreateTime(new Date());
//        substanceStateLimitEntity3.setUpdateTime(new Date());
//        substanceStateLimitEntity3.setDeleteFlag(DeleteFlag.NO.ordinal());
//
//        SubstanceStateLimitEntity substanceStateLimitEntity4 = new SubstanceStateLimitEntity();
//        substanceStateLimitEntity4.setSubstanceInfoId(substanceInfoId);
//        substanceStateLimitEntity4.setConditionNote("高温");
//        substanceStateLimitEntity4.setContactTimeRate(25);
//        substanceStateLimitEntity4.setLaborIntensityOne(33);
//        substanceStateLimitEntity4.setLaborIntensityTwo(32);
//        substanceStateLimitEntity4.setLaborIntensityThree(31);
//        substanceStateLimitEntity4.setLaborIntensityFour(30);
//        substanceStateLimitEntity4.setRemark("");
//        substanceStateLimitEntity4.setCreateBy("");
//        substanceStateLimitEntity4.setUpdateBy("");
//        substanceStateLimitEntity4.setCreateTime(new Date());
//        substanceStateLimitEntity4.setUpdateTime(new Date());
//        substanceStateLimitEntity4.setDeleteFlag(DeleteFlag.NO.ordinal());
//
//        substanceStateLimitEntityList.add(substanceStateLimitEntity1);
//        substanceStateLimitEntityList.add(substanceStateLimitEntity2);
//        substanceStateLimitEntityList.add(substanceStateLimitEntity3);
//        substanceStateLimitEntityList.add(substanceStateLimitEntity4);
//
//        substanceStateLimitService.saveBatch(substanceStateLimitEntityList);
//
//        //法律法规
//        saveSubstanceTestLawEntityWuli(substanceInfoId,"GBZ/T 189.7","工作场所物理因素测量 高温",TestCategory.CATEGORY_9.getCode());
//    }
//    private void handStype_5() {
//        //物质信息
//        Long substanceInfoId = saveSubstanceInfoEntityWuli("紫外辐射",SubstanceType.ULTRAVIOLET_RADIATION.ordinal());
//        //物质限值
//        List<SubstanceStateLimitEntity> substanceStateLimitEntityList = new ArrayList<>();
//        SubstanceStateLimitEntity substanceStateLimitEntity1 = new SubstanceStateLimitEntity();
//        substanceStateLimitEntity1.setSubstanceInfoId(substanceInfoId);
//        substanceStateLimitEntity1.setConditionNote("紫外辐射|中波紫外线(280nm≤λ<315nm)");
//        substanceStateLimitEntity1.setIrradiance("0.26");
//        substanceStateLimitEntity1.setExposure("3.7");
//        substanceStateLimitEntity1.setContactTime(8);
//        substanceStateLimitEntity1.setRemark("");
//        substanceStateLimitEntity1.setCreateBy("");
//        substanceStateLimitEntity1.setUpdateBy("");
//        substanceStateLimitEntity1.setCreateTime(new Date());
//        substanceStateLimitEntity1.setUpdateTime(new Date());
//        substanceStateLimitEntity1.setDeleteFlag(DeleteFlag.NO.ordinal());
//
//        SubstanceStateLimitEntity substanceStateLimitEntity2 = new SubstanceStateLimitEntity();
//        substanceStateLimitEntity2.setSubstanceInfoId(substanceInfoId);
//        substanceStateLimitEntity2.setConditionNote("紫外辐射|短波紫外线(100nm≤λ<280nm)");
//        substanceStateLimitEntity2.setIrradiance("0.13");
//        substanceStateLimitEntity2.setExposure("1.8");
//        substanceStateLimitEntity2.setContactTime(8);
//        substanceStateLimitEntity2.setRemark("");
//        substanceStateLimitEntity2.setCreateBy("");
//        substanceStateLimitEntity2.setUpdateBy("");
//        substanceStateLimitEntity2.setCreateTime(new Date());
//        substanceStateLimitEntity2.setUpdateTime(new Date());
//        substanceStateLimitEntity2.setDeleteFlag(DeleteFlag.NO.ordinal());
//
//        SubstanceStateLimitEntity substanceStateLimitEntity3 = new SubstanceStateLimitEntity();
//        substanceStateLimitEntity3.setSubstanceInfoId(substanceInfoId);
//        substanceStateLimitEntity3.setConditionNote("紫外辐射|电焊弧光");
//        //substanceStateLimitEntity3.setSpectrumClassification("电焊弧光");
//        substanceStateLimitEntity3.setIrradiance("0.24");
//        substanceStateLimitEntity3.setExposure("3.5");
//        substanceStateLimitEntity3.setRemark("");
//        substanceStateLimitEntity3.setCreateBy("");
//        substanceStateLimitEntity3.setUpdateBy("");
//        substanceStateLimitEntity3.setCreateTime(new Date());
//        substanceStateLimitEntity3.setUpdateTime(new Date());
//        substanceStateLimitEntity3.setDeleteFlag(DeleteFlag.NO.ordinal());
//
//        substanceStateLimitEntityList.add(substanceStateLimitEntity1);
//        substanceStateLimitEntityList.add(substanceStateLimitEntity2);
//        substanceStateLimitEntityList.add(substanceStateLimitEntity3);
//
//        substanceStateLimitService.saveBatch(substanceStateLimitEntityList);
//
//        //法律法规
//        saveSubstanceTestLawEntityWuli(substanceInfoId,"GBZ/T 189.6","工作场所物理因素测量 紫外辐射",TestCategory.CATEGORY_9.getCode());
//    }
//    private void handStype_6() {
//        //物质信息
//        Long substanceInfoId = saveSubstanceInfoEntityWuli("手传振动",SubstanceType.HAND_VIBRATION.ordinal());
//        //物质限值
//        SubstanceStateLimitEntity substanceStateLimitEntity1 = new SubstanceStateLimitEntity();
//        substanceStateLimitEntity1.setSubstanceInfoId(substanceInfoId);
//        substanceStateLimitEntity1.setConditionNote("手传振动");
//        substanceStateLimitEntity1.setContactTime(4);
//        substanceStateLimitEntity1.setAcceleration(5);
//        substanceStateLimitEntity1.setCreateBy("");
//        substanceStateLimitEntity1.setUpdateBy("");
//        substanceStateLimitEntity1.setCreateTime(new Date());
//        substanceStateLimitEntity1.setUpdateTime(new Date());
//        substanceStateLimitEntity1.setDeleteFlag(DeleteFlag.NO.ordinal());
//        substanceStateLimitService.save(substanceStateLimitEntity1);
//
//        //法律法规
//        saveSubstanceTestLawEntityWuli(substanceInfoId,"GBZ/T 189.9","工作场所物理因素测量 手传振动",TestCategory.CATEGORY_9.getCode());
//    }
//    private void handStype_7() {
//        //物质信息
//        Long substanceInfoId = saveSubstanceInfoEntityWuli("工频电场",SubstanceType.POWER_FREQUENCY_ELECTRIC.ordinal());
//        //物质限值
//        SubstanceStateLimitEntity substanceStateLimitEntity1 = new SubstanceStateLimitEntity();
//        substanceStateLimitEntity1.setSubstanceInfoId(substanceInfoId);
//        substanceStateLimitEntity1.setConditionNote("工频电场");
//        substanceStateLimitEntity1.setContactTime(8);
//        substanceStateLimitEntity1.setFrequency("50");
//        substanceStateLimitEntity1.setElectricFieldIntensity(5);
//        substanceStateLimitEntity1.setCreateBy("");
//        substanceStateLimitEntity1.setUpdateBy("");
//        substanceStateLimitEntity1.setCreateTime(new Date());
//        substanceStateLimitEntity1.setUpdateTime(new Date());
//        substanceStateLimitEntity1.setDeleteFlag(DeleteFlag.NO.ordinal());
//        substanceStateLimitService.save(substanceStateLimitEntity1);
//
//        //法律法规
//        saveSubstanceTestLawEntityWuli(substanceInfoId,"GBZ/T 189.3","工作场所物理因素测量 工频电场",TestCategory.CATEGORY_9.getCode());
//
//    }
//    private void handStype_8() {
//        //物质信息
//        Long substanceInfoId = saveSubstanceInfoEntityWuli("高频电磁场",SubstanceType.HIGH_ELECTROMAGNETIC.ordinal());
//        List<SubstanceStateLimitEntity> substanceStateLimitEntityList = new ArrayList<>();
//        //物质限值
//        SubstanceStateLimitEntity substanceStateLimitEntity1 = new SubstanceStateLimitEntity();
//        substanceStateLimitEntity1.setSubstanceInfoId(substanceInfoId);
//        substanceStateLimitEntity1.setConditionNote("高频电磁场");
//        substanceStateLimitEntity1.setContactTime(8);
//        substanceStateLimitEntity1.setFrequency("0.1≤f≤3.0");
//        substanceStateLimitEntity1.setElectricFieldIntensity(50);
//        substanceStateLimitEntity1.setMagneticFieldIntensity("5");
//        substanceStateLimitEntity1.setCreateBy("");
//        substanceStateLimitEntity1.setUpdateBy("");
//        substanceStateLimitEntity1.setCreateTime(new Date());
//        substanceStateLimitEntity1.setUpdateTime(new Date());
//        substanceStateLimitEntity1.setDeleteFlag(DeleteFlag.NO.ordinal());
//
//        SubstanceStateLimitEntity substanceStateLimitEntity2 = new SubstanceStateLimitEntity();
//        substanceStateLimitEntity2.setSubstanceInfoId(substanceInfoId);
//        substanceStateLimitEntity2.setConditionNote("高频电磁场");
//        substanceStateLimitEntity2.setContactTime(8);
//        substanceStateLimitEntity2.setFrequency("3.0<f≤30");
//        substanceStateLimitEntity2.setElectricFieldIntensity(25);
//        substanceStateLimitEntity2.setMagneticFieldIntensity("-");
//        substanceStateLimitEntity2.setCreateBy("");
//        substanceStateLimitEntity2.setUpdateBy("");
//        substanceStateLimitEntity2.setCreateTime(new Date());
//        substanceStateLimitEntity2.setUpdateTime(new Date());
//        substanceStateLimitEntity2.setDeleteFlag(DeleteFlag.NO.ordinal());
//
//        substanceStateLimitEntityList.add(substanceStateLimitEntity1);
//        substanceStateLimitEntityList.add(substanceStateLimitEntity2);
//        substanceStateLimitService.saveBatch(substanceStateLimitEntityList);
//
//        //法律法规
//        saveSubstanceTestLawEntityWuli(substanceInfoId,"GBZ/T 189.2","工作场所物理因素测量 高频电磁场",TestCategory.CATEGORY_9.getCode());
//    }
//    private void handStype_9() {
//        //物质信息
//        Long substanceInfoId = saveSubstanceInfoEntityWuli("超高频辐射",SubstanceType.ULTRA_HIGH_FREQUENCY_RADIATION.ordinal());
//        List<SubstanceStateLimitEntity> substanceStateLimitEntityList = new ArrayList<>();
//        //物质限值
//        SubstanceStateLimitEntity substanceStateLimitEntity1 = new SubstanceStateLimitEntity();
//        substanceStateLimitEntity1.setSubstanceInfoId(substanceInfoId);
//        substanceStateLimitEntity1.setConditionNote("超高频辐射|连续波");
//        substanceStateLimitEntity1.setContactTime(8);
//        substanceStateLimitEntity1.setPowerDensity("0.05");
//        substanceStateLimitEntity1.setElectricFieldIntensity(14);
//        substanceStateLimitEntity1.setCreateBy("");
//        substanceStateLimitEntity1.setUpdateBy("");
//        substanceStateLimitEntity1.setCreateTime(new Date());
//        substanceStateLimitEntity1.setUpdateTime(new Date());
//        substanceStateLimitEntity1.setDeleteFlag(DeleteFlag.NO.ordinal());
//
//        SubstanceStateLimitEntity substanceStateLimitEntity2 = new SubstanceStateLimitEntity();
//        substanceStateLimitEntity2.setSubstanceInfoId(substanceInfoId);
//        substanceStateLimitEntity2.setConditionNote("超高频辐射|连续波");
//        substanceStateLimitEntity2.setContactTime(4);
//        substanceStateLimitEntity2.setPowerDensity("0.1");
//        substanceStateLimitEntity2.setElectricFieldIntensity(19);
//        substanceStateLimitEntity2.setCreateBy("");
//        substanceStateLimitEntity2.setUpdateBy("");
//        substanceStateLimitEntity2.setCreateTime(new Date());
//        substanceStateLimitEntity2.setUpdateTime(new Date());
//        substanceStateLimitEntity2.setDeleteFlag(DeleteFlag.NO.ordinal());
//
//        SubstanceStateLimitEntity substanceStateLimitEntity3 = new SubstanceStateLimitEntity();
//        substanceStateLimitEntity3.setSubstanceInfoId(substanceInfoId);
//        substanceStateLimitEntity3.setConditionNote("超高频辐射|脉冲波");
//        substanceStateLimitEntity3.setContactTime(8);
//        substanceStateLimitEntity3.setPowerDensity("0.025");
//        substanceStateLimitEntity3.setElectricFieldIntensity(10);
//        substanceStateLimitEntity3.setCreateBy("");
//        substanceStateLimitEntity3.setUpdateBy("");
//        substanceStateLimitEntity3.setCreateTime(new Date());
//        substanceStateLimitEntity3.setUpdateTime(new Date());
//        substanceStateLimitEntity3.setDeleteFlag(DeleteFlag.NO.ordinal());
//
//        SubstanceStateLimitEntity substanceStateLimitEntity4 = new SubstanceStateLimitEntity();
//        substanceStateLimitEntity4.setSubstanceInfoId(substanceInfoId);
//        substanceStateLimitEntity4.setConditionNote("超高频辐射|脉冲波");
//        substanceStateLimitEntity4.setContactTime(4);
//        substanceStateLimitEntity4.setPowerDensity("0.05");
//        substanceStateLimitEntity4.setElectricFieldIntensity(14);
//        substanceStateLimitEntity4.setCreateBy("");
//        substanceStateLimitEntity4.setUpdateBy("");
//        substanceStateLimitEntity4.setCreateTime(new Date());
//        substanceStateLimitEntity4.setUpdateTime(new Date());
//        substanceStateLimitEntity4.setDeleteFlag(DeleteFlag.NO.ordinal());
//
//        substanceStateLimitEntityList.add(substanceStateLimitEntity1);
//        substanceStateLimitEntityList.add(substanceStateLimitEntity2);
//        substanceStateLimitEntityList.add(substanceStateLimitEntity3);
//        substanceStateLimitEntityList.add(substanceStateLimitEntity4);
//        substanceStateLimitService.saveBatch(substanceStateLimitEntityList);
//
//        //法律法规
//        saveSubstanceTestLawEntityWuli(substanceInfoId,"GBZ/T 189.1","工作场所物理因素测量 超高频辐射",TestCategory.CATEGORY_9.getCode());
//    }
//    private void handStype_10() {
//        //物质信息
//        Long substanceInfoId = saveSubstanceInfoEntityWuli("微波辐射",SubstanceType.MICROWAVE_RADIATION.ordinal());
//        //物质限值
//        List<SubstanceStateLimitEntity> substanceStateLimitEntityList = new ArrayList<>();
//        SubstanceStateLimitEntity substanceStateLimitEntity1 = new SubstanceStateLimitEntity();
//        substanceStateLimitEntity1.setSubstanceInfoId(substanceInfoId);
//        substanceStateLimitEntity1.setConditionNote("全身辐射|连续微波");
//        substanceStateLimitEntity1.setDailyDose(400);
//        substanceStateLimitEntity1.setAveragePowerDensity(50);
//        substanceStateLimitEntity1.setNonAveragePowerDensity("400/t");
//        substanceStateLimitEntity1.setShortPowerDensity(5);
//        substanceStateLimitEntity1.setRemark("t为受辐射时间，单位为h");
//        substanceStateLimitEntity1.setCreateBy("");
//        substanceStateLimitEntity1.setUpdateBy("");
//        substanceStateLimitEntity1.setCreateTime(new Date());
//        substanceStateLimitEntity1.setUpdateTime(new Date());
//        substanceStateLimitEntity1.setDeleteFlag(DeleteFlag.NO.ordinal());
//
//        SubstanceStateLimitEntity substanceStateLimitEntity2 = new SubstanceStateLimitEntity();
//        substanceStateLimitEntity2.setSubstanceInfoId(substanceInfoId);
//        substanceStateLimitEntity2.setConditionNote("全身辐射|脉冲微波");
//        substanceStateLimitEntity2.setDailyDose(200);
//        substanceStateLimitEntity2.setAveragePowerDensity(25);
//        substanceStateLimitEntity2.setNonAveragePowerDensity("200/t");
//        substanceStateLimitEntity2.setShortPowerDensity(5);
//        substanceStateLimitEntity2.setRemark("t为受辐射时间，单位为h");
//        substanceStateLimitEntity2.setCreateBy("");
//        substanceStateLimitEntity2.setUpdateBy("");
//        substanceStateLimitEntity2.setCreateTime(new Date());
//        substanceStateLimitEntity2.setUpdateTime(new Date());
//        substanceStateLimitEntity2.setDeleteFlag(DeleteFlag.NO.ordinal());
//
//        SubstanceStateLimitEntity substanceStateLimitEntity3 = new SubstanceStateLimitEntity();
//        substanceStateLimitEntity3.setSubstanceInfoId(substanceInfoId);
//        substanceStateLimitEntity3.setConditionNote("肢体局部辐射|连续微波或脉冲微波");
//        substanceStateLimitEntity3.setDailyDose(4000);
//        substanceStateLimitEntity3.setAveragePowerDensity(500);
//        substanceStateLimitEntity3.setNonAveragePowerDensity("4000/t");
//        substanceStateLimitEntity3.setShortPowerDensity(5);
//        substanceStateLimitEntity3.setRemark("t为受辐射时间，单位为h");
//        substanceStateLimitEntity3.setCreateBy("");
//        substanceStateLimitEntity3.setUpdateBy("");
//        substanceStateLimitEntity3.setCreateTime(new Date());
//        substanceStateLimitEntity3.setUpdateTime(new Date());
//        substanceStateLimitEntity3.setDeleteFlag(DeleteFlag.NO.ordinal());
//
//        substanceStateLimitEntityList.add(substanceStateLimitEntity1);
//        substanceStateLimitEntityList.add(substanceStateLimitEntity2);
//        substanceStateLimitEntityList.add(substanceStateLimitEntity3);
//
//        substanceStateLimitService.saveBatch(substanceStateLimitEntityList);
//
//        //法律法规
//        saveSubstanceTestLawEntityWuli(substanceInfoId,"GBZ/T 189.5","工作场所物理因素测量 微波辐射",TestCategory.CATEGORY_9.getCode());
//    }
//    private void handStype_11() {
//        //物质信息
//        Long substanceInfoId = saveSubstanceInfoEntityWuli("激光辐射",SubstanceType.LASER_RADIATION.ordinal());
//
//        List<SubstanceStateLimitEntity> substanceStateLimitEntityList = new ArrayList<>();
//        for(int i = 0;i < 5;i++){
//            SubstanceStateLimitEntity substanceStateLimitEntity1 = new SubstanceStateLimitEntity();
//            substanceStateLimitEntity1.setSubstanceInfoId(substanceInfoId);
//            substanceStateLimitEntity1.setConditionNote("激光辐射|紫外线");
//            substanceStateLimitEntity1.setRemark("t为照射时间");
//            substanceStateLimitEntity1.setLaserExposureSite("眼直视激光束");
//            substanceStateLimitEntity1.setContactTime(8);
//            substanceStateLimitEntity1.setCreateBy("");
//            substanceStateLimitEntity1.setUpdateBy("");
//            substanceStateLimitEntity1.setCreateTime(new Date());
//            substanceStateLimitEntity1.setUpdateTime(new Date());
//            substanceStateLimitEntity1.setDeleteFlag(DeleteFlag.NO.ordinal());
//
//            substanceStateLimitEntityList.add(substanceStateLimitEntity1);
//
//        }
//        for(int i = 0;i < 6;i++){
//            SubstanceStateLimitEntity substanceStateLimitEntity1 = new SubstanceStateLimitEntity();
//            substanceStateLimitEntity1.setSubstanceInfoId(substanceInfoId);
//            substanceStateLimitEntity1.setConditionNote("激光辐射|红外线");
//            substanceStateLimitEntity1.setRemark("t为照射时间");
//            substanceStateLimitEntity1.setLaserExposureSite("眼直视激光束");
//            substanceStateLimitEntity1.setContactTime(8);
//            substanceStateLimitEntity1.setCreateBy("");
//            substanceStateLimitEntity1.setUpdateBy("");
//            substanceStateLimitEntity1.setCreateTime(new Date());
//            substanceStateLimitEntity1.setUpdateTime(new Date());
//            substanceStateLimitEntity1.setDeleteFlag(DeleteFlag.NO.ordinal());
//
//            SubstanceStateLimitEntity substanceStateLimitEntity2 = new SubstanceStateLimitEntity();
//            substanceStateLimitEntity2.setSubstanceInfoId(substanceInfoId);
//            substanceStateLimitEntity2.setConditionNote("激光辐射|可见光");
//            substanceStateLimitEntity2.setRemark("t为照射时间");
//            substanceStateLimitEntity2.setLaserExposureSite("眼直视激光束");
//            substanceStateLimitEntity2.setContactTime(8);
//            substanceStateLimitEntity2.setCreateBy("");
//            substanceStateLimitEntity2.setUpdateBy("");
//            substanceStateLimitEntity2.setCreateTime(new Date());
//            substanceStateLimitEntity2.setUpdateTime(new Date());
//            substanceStateLimitEntity2.setDeleteFlag(DeleteFlag.NO.ordinal());
//
//            substanceStateLimitEntityList.add(substanceStateLimitEntity1);
//            substanceStateLimitEntityList.add(substanceStateLimitEntity2);
//        }
//        for(int i = 0;i < 3;i++){
//            SubstanceStateLimitEntity substanceStateLimitEntity1 = new SubstanceStateLimitEntity();
//            substanceStateLimitEntity1.setSubstanceInfoId(substanceInfoId);
//            substanceStateLimitEntity1.setConditionNote("激光辐射|远红外线");
//            substanceStateLimitEntity1.setRemark("t为照射时间");
//            substanceStateLimitEntity1.setLaserExposureSite("眼直视激光束");
//            substanceStateLimitEntity1.setContactTime(8);
//            substanceStateLimitEntity1.setCreateBy("");
//            substanceStateLimitEntity1.setUpdateBy("");
//            substanceStateLimitEntity1.setCreateTime(new Date());
//            substanceStateLimitEntity1.setUpdateTime(new Date());
//            substanceStateLimitEntity1.setDeleteFlag(DeleteFlag.NO.ordinal());
//            substanceStateLimitEntityList.add(substanceStateLimitEntity1);
//        }
//        for(int i = 0;i < 5;i++){
//            SubstanceStateLimitEntity substanceStateLimitEntity1 = new SubstanceStateLimitEntity();
//            substanceStateLimitEntity1.setSubstanceInfoId(substanceInfoId);
//            substanceStateLimitEntity1.setConditionNote("激光辐射|紫外线");
//            substanceStateLimitEntity1.setRemark("t为照射时间");
//            substanceStateLimitEntity1.setLaserExposureSite("激光照射皮肤");
//            substanceStateLimitEntity1.setContactTime(8);
//            substanceStateLimitEntity1.setCreateBy("");
//            substanceStateLimitEntity1.setUpdateBy("");
//            substanceStateLimitEntity1.setCreateTime(new Date());
//            substanceStateLimitEntity1.setUpdateTime(new Date());
//            substanceStateLimitEntity1.setDeleteFlag(DeleteFlag.NO.ordinal());
//
//            substanceStateLimitEntityList.add(substanceStateLimitEntity1);
//        }
//        for(int i = 0;i < 3;i++){
//            SubstanceStateLimitEntity substanceStateLimitEntity1 = new SubstanceStateLimitEntity();
//            substanceStateLimitEntity1.setSubstanceInfoId(substanceInfoId);
//            substanceStateLimitEntity1.setConditionNote("激光辐射|远红外线");
//            substanceStateLimitEntity1.setRemark("t为照射时间");
//            substanceStateLimitEntity1.setLaserExposureSite("激光照射皮肤");
//            substanceStateLimitEntity1.setContactTime(8);
//            substanceStateLimitEntity1.setCreateBy("");
//            substanceStateLimitEntity1.setUpdateBy("");
//            substanceStateLimitEntity1.setCreateTime(new Date());
//            substanceStateLimitEntity1.setUpdateTime(new Date());
//            substanceStateLimitEntity1.setDeleteFlag(DeleteFlag.NO.ordinal());
//            substanceStateLimitEntityList.add(substanceStateLimitEntity1);
//        }
//        for(int i = 0;i < 9;i++){
//            SubstanceStateLimitEntity substanceStateLimitEntity1 = new SubstanceStateLimitEntity();
//            substanceStateLimitEntity1.setSubstanceInfoId(substanceInfoId);
//            substanceStateLimitEntity1.setConditionNote("激光辐射|可见光与红外线");
//            substanceStateLimitEntity1.setRemark("t为照射时间");
//            substanceStateLimitEntity1.setLaserExposureSite("激光照射皮肤");
//            substanceStateLimitEntity1.setContactTime(8);
//            substanceStateLimitEntity1.setCreateBy("");
//            substanceStateLimitEntity1.setUpdateBy("");
//            substanceStateLimitEntity1.setCreateTime(new Date());
//            substanceStateLimitEntity1.setUpdateTime(new Date());
//            substanceStateLimitEntity1.setDeleteFlag(DeleteFlag.NO.ordinal());
//            substanceStateLimitEntityList.add(substanceStateLimitEntity1);
//        }
//        substanceStateLimitService.saveBatch(substanceStateLimitEntityList);
//        //法律法规
//        saveSubstanceTestLawEntityWuli(substanceInfoId,"GBZ/T 189.4","工作场所物理因素测量 微波辐射",TestCategory.CATEGORY_9.getCode());
//
//    }
//
//    private void saveSubstanceTestLawEntityWuli(Long substanceInfoId,String testStandards, String testStandardsName,int testCategory) {
//        SubstanceTestLawEntity substanceTestLawEntity = new SubstanceTestLawEntity();
//        substanceTestLawEntity.setSubstanceInfoId(substanceInfoId);
//        substanceTestLawEntity.setTestStandards(testStandards);
//        substanceTestLawEntity.setTestStandardsName(testStandardsName);
//        substanceTestLawEntity.setLegalEffect("国家标准");
//        substanceTestLawEntity.setStatus(1);
//        substanceTestLawEntity.setCreateBy("");
//        substanceTestLawEntity.setUpdateBy("");
//        substanceTestLawEntity.setCreateTime(new Date());
//        substanceTestLawEntity.setUpdateTime(new Date());
//        substanceTestLawEntity.setDeleteFlag(DeleteFlag.NO.ordinal());
//        substanceTestLawEntity.setTestCategory(testCategory);
//        substanceTestLawService.save(substanceTestLawEntity);
//    }
//
//    private Long saveSubstanceInfoEntityWuli(String substanceName, int substanceType) {
//        SubstanceInfoEntity substanceInfoEntity = new SubstanceInfoEntity();
//        substanceInfoEntity.setSubstanceName(substanceName);
//        substanceInfoEntity.setSubstanceEnglishName("");
//        substanceInfoEntity.setSubstanceOtherName("");
//        substanceInfoEntity.setCasCode("");
//        substanceInfoEntity.setHealthImpact("");
//        substanceInfoEntity.setOccupationalDisease("");
//        substanceInfoEntity.setSubstanceType(substanceType);
//        substanceInfoEntity.setIsReduction(IsReduction.NO.ordinal());
//        substanceInfoEntity.setCreateBy("");
//        substanceInfoEntity.setUpdateBy("");
//        substanceInfoEntity.setCreateTime(new Date());
//        substanceInfoEntity.setUpdateTime(new Date());
//        substanceInfoEntity.setDeleteFlag(DeleteFlag.NO.ordinal());
//        substanceInfoService.save(substanceInfoEntity);
//        return substanceInfoEntity.getId();
//    }
//
//    private Long saveSubstanceInfoEntity(SubstanceEntity substanceEntity, SubstanceType substanceType){
//        SubstanceInfoEntity substanceInfoEntity = new SubstanceInfoEntity();
//        substanceInfoEntity.setSubstanceName(substanceEntity.getName());
//        substanceInfoEntity.setSubstanceEnglishName(substanceEntity.getNameEn());
//        substanceInfoEntity.setSubstanceOtherName("");
//        substanceInfoEntity.setCasCode(substanceEntity.getCasNo());
//        substanceInfoEntity.setHealthImpact("");
//        substanceInfoEntity.setOccupationalDisease("");
//        substanceInfoEntity.setSubstanceType(substanceType.ordinal());
//        int isReduction = 1;
//        if(substanceEntity.getDeduction() != null && substanceEntity.getDeduction() == 2 ){
//            isReduction = 0;
//        }
//        substanceInfoEntity.setIsReduction(isReduction);
//        substanceInfoEntity.setCreateBy("");
//        substanceInfoEntity.setUpdateBy("");
//        substanceInfoEntity.setCreateTime(new Date());
//        substanceInfoEntity.setUpdateTime(new Date());
//        substanceInfoEntity.setDeleteFlag(DeleteFlag.NO.ordinal());
//        substanceInfoService.save(substanceInfoEntity);
//        return substanceInfoEntity.getId();
//    }
//
//    private void saveSubstanceStateLimitEntity(SubstanceEntity substanceEntity, Long substanceInfoId) {
//        SubstanceStateLimitEntity substanceStateLimitEntity = new SubstanceStateLimitEntity();
//        substanceStateLimitEntity.setSubstanceInfoId(substanceInfoId);
//        substanceStateLimitEntity.setMac(substanceEntity.getMac());
//        substanceStateLimitEntity.setPcStel(substanceEntity.getPcStel());
//        substanceStateLimitEntity.setPcTwa(substanceEntity.getPcTwa());
//        substanceStateLimitEntity.setConditionNote(substanceEntity.getName());
//        substanceStateLimitEntity.setAdverseReactions(substanceEntity.getReaction());
//        substanceStateLimitEntity.setRemark(substanceEntity.getRemarksNote());
//        substanceStateLimitEntity.setCreateBy("");
//        substanceStateLimitEntity.setUpdateBy("");
//        substanceStateLimitEntity.setCreateTime(new Date());
//        substanceStateLimitEntity.setUpdateTime(new Date());
//        substanceStateLimitEntity.setDeleteFlag(DeleteFlag.NO.ordinal());
//        substanceStateLimitService.save(substanceStateLimitEntity);
//    }
//
//    private Long saveSubstanceTestLawEntity(SubstanceDetectionEntity substanceDetectionEntity, Long substanceInfoId) {
//        SubstanceTestLawEntity substanceTestLawEntity = new SubstanceTestLawEntity();
//        substanceTestLawEntity.setSubstanceInfoId(substanceInfoId);
//        substanceTestLawEntity.setTestStandards(substanceDetectionEntity.getStandardSerialNum());
//        substanceTestLawEntity.setTestStandardsName(substanceDetectionEntity.getStandardName());
//        substanceTestLawEntity.setLegalEffect("国家标准");
//        substanceTestLawEntity.setStatus(1);
//        substanceTestLawEntity.setCreateBy("");
//        substanceTestLawEntity.setUpdateBy("");
//        substanceTestLawEntity.setCreateTime(new Date());
//        substanceTestLawEntity.setUpdateTime(new Date());
//        substanceTestLawEntity.setDeleteFlag(DeleteFlag.NO.ordinal());
//        substanceTestLawEntity.setTestCategory(TestCategory.CATEGORY_8.getCode());
//        substanceTestLawService.save(substanceTestLawEntity);
//        return substanceTestLawEntity.getId();
//    }
//
//    private void saveSubstanceTestMethodEntity(SubstanceDetectionEntity substanceDetectionEntity, Long substanceTestLawId) {
//        SubstanceTestMethodEntity substanceTestMethodEntity = new SubstanceTestMethodEntity();
//        substanceTestMethodEntity.setSubstanceTestLawId(substanceTestLawId);
//        String standard = substanceDetectionEntity.getStandard();
//        String testNumber = "";
//        if(StringUtils.isNotBlank(standard) && standard.contains("（")){
//            int index = standard.indexOf("（");
//            testNumber = standard.substring(index);
//        }
//        substanceTestMethodEntity.setTestNumber(testNumber);
//        substanceTestMethodEntity.setTestName(substanceDetectionEntity.getDetectionMethod());
//        substanceTestMethodEntity.setIsDirectReading(IsDirectReadingType.NO.getCode());
//        substanceTestMethodEntity.setIsPersonalSample(substanceDetectionEntity.getIndSample().intValue());
//        if(StringUtils.isNotBlank(substanceDetectionEntity.getFlow())){
//            substanceTestMethodEntity.setIsAreaSample(IsAreaSampleType.YES.getCode());
//        }else{
//            substanceTestMethodEntity.setIsAreaSample(IsAreaSampleType.NO.getCode());
//        }
//        substanceTestMethodEntity.setFixedSampleDuration(substanceDetectionEntity.getTestTime());
//        substanceTestMethodEntity.setFixedSampleTraffic(substanceDetectionEntity.getFlow());
//        substanceTestMethodEntity.setFixedSampleNote(substanceDetectionEntity.getTestTimeNote());
//        substanceTestMethodEntity.setFixedSampleEquipment(substanceDetectionEntity.getEquipment());
//        substanceTestMethodEntity.setPersonalSampleDuration(substanceDetectionEntity.getIndTestTime());
//        substanceTestMethodEntity.setPersonalSampleNote(substanceDetectionEntity.getIndTestTimeNote());
//        substanceTestMethodEntity.setPersonalSampleTraffic(substanceDetectionEntity.getIndFlow());
//        substanceTestMethodEntity.setPersonalSampleEquipment(substanceDetectionEntity.getIndEquipment());
//        substanceTestMethodEntity.setBlankSample("3");
//        substanceTestMethodEntity.setSaveMethod(substanceDetectionEntity.getPreserveTraffic());
//        substanceTestMethodEntity.setSaveRequirement(substanceDetectionEntity.getPreserveRequire());
//        substanceTestMethodEntity.setSaveTerm(String.valueOf(substanceDetectionEntity.getShelfLife() == null ? "" : substanceDetectionEntity.getShelfLife()));
//        substanceTestMethodEntity.setCollector(substanceDetectionEntity.getCollector());
//        substanceTestMethodEntity.setCreateBy("");
//        substanceTestMethodEntity.setUpdateBy("");
//        substanceTestMethodEntity.setCreateTime(new Date());
//        substanceTestMethodEntity.setUpdateTime(new Date());
//        substanceTestMethodEntity.setDeleteFlag(DeleteFlag.NO.ordinal());
//        substanceTestMethodService.save(substanceTestMethodEntity);
//    }
//
//
//}
