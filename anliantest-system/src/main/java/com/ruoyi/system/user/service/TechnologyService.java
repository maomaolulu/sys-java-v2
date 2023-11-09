package com.ruoyi.system.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.system.user.entity.TechnologyEntity;
import com.ruoyi.system.user.vo.TechnologyVo;

import java.util.List;

/**
 * @author ZhuYiCheng
 * @date 2023/4/28 16:36
 */
public interface TechnologyService extends IService<TechnologyEntity> {


    /**
     * 校验证书编号
     * @param code
     * @return
     */
    Boolean checkCode(String code);

    /**
     * 新增技术档案
     * @param technologyEntity
     */
    void addTechnology(TechnologyEntity technologyEntity);

    /**
     * 批量新增技术档案
     * @param technologyVo
     */
    void addTechnologyBatch(TechnologyVo technologyVo);

    /**
     * 技术档案列表(不分页)
     * @param userId
     * @return
     */
    List<TechnologyEntity> getFileList(Long userId);

    /**
     * 修改技术档案
     * @param technologyVo
     */
    void updateTechnology(TechnologyVo technologyVo);

    /**
     * 删除技术档案
     * @param userId
     */
    void deleteTechnology(Long userId);


}
