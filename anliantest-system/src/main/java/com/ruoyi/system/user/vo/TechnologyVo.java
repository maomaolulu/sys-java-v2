package com.ruoyi.system.user.vo;

import com.ruoyi.system.user.entity.TechnologyEntity;
import lombok.Data;

import java.util.List;

/**
 * @author ZhuYiCheng
 * @date 2023/4/28 16:39
 */
@Data
public class TechnologyVo {

    /**
     * 技术档案列表
     */
    private List<TechnologyEntity> technologyEntityList;
}
