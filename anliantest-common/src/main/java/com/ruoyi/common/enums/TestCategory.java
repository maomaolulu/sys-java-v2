package com.ruoyi.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 检测类别
 */
@Getter
@RequiredArgsConstructor
public enum TestCategory {

    CATEGORY_1(1,"公共场所"),
    CATEGORY_2(2,"公共场所集中空调通风系统"),
    CATEGORY_3(3,"人防工程"),
    CATEGORY_4(4,"水和废水"),
    CATEGORY_5(5,"生活饮用水"),
    CATEGORY_6(6,"一次性使用卫生用品"),
    CATEGORY_7(7,"消毒效果"),
    CATEGORY_8(8,"工作场所空气"),
    CATEGORY_9(9,"工作场所物理因素"),
    CATEGORY_10(10,"工作场所通风工程"),
    CATEGORY_11(11,"工作场所环境卫生条件"),
    CATEGORY_12(12,"环境空气和废气"),
    CATEGORY_13(13,"土壤"),
    CATEGORY_14(14,"固体废物"),
    CATEGORY_15(15,"噪声"),
    CATEGORY_16(16,"教室环境卫生"),
    CATEGORY_17(17,"学校厕所"),
    CATEGORY_18(18,"学生宿舍"),
    CATEGORY_19(19,"加油站油气回收"),
    CATEGORY_20(20,"室内空气"),
    CATEGORY_21(21,"固体废物"),
    CATEGORY_22(22,"水和废水（包括地下水）"),
    CATEGORY_23(23,"水（含大气降水）和废水"),
    CATEGORY_24(24,"水和废水（含大气降水）"),
    CATEGORY_25(25,"空气和废气"),
    CATEGORY_26(26,"土壤和沉积物"),
    CATEGORY_27(27,"振动"),
    CATEGORY_28(28,"洁净室（区）");
    /**
     * 类型
     */
    private final Integer code;
    /**
     * 描述
     */
    private final String description;
}
