package com.ruoyi.system.user.vo;

import com.ruoyi.system.user.entity.PersonnelEntity;
import lombok.Data;

import java.util.List;

/**
 * @author ZhuYiCheng
 * @date 2023/4/28 15:18
 */
@Data
public class PersonnelVo {

    /**
     * 人事档案列表
     */
    private List<PersonnelEntity> personnelEntityList;
}
