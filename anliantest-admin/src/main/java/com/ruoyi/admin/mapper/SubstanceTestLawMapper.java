package com.ruoyi.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.admin.domain.dto.SubstanceTestLawDto;
import com.ruoyi.admin.entity.SubstanceTestLawEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author gy
 * @date 2023-06-06
 */
@Mapper
public interface SubstanceTestLawMapper extends BaseMapper<SubstanceTestLawEntity> {
    /**
     * 条件查询
     * @param substanceTestLawDto
     * @return List<SubstanceTestLawEntity>
     */
    List<SubstanceTestLawEntity> listByCondition(SubstanceTestLawDto substanceTestLawDto);

    /**
     * 新增检测标准/法律法规
     * @param substanceTestLawDto
     * @return boolean
     */
    Boolean saveByCondition(SubstanceTestLawDto substanceTestLawDto);

    /**
     * 修改检测标准/法律法规
     * @param substanceTestLawDto
     * @return boolean
     */
    Boolean updateByCondition(SubstanceTestLawDto substanceTestLawDto);

    /**
     * 删除检测标准/法律法规
     * @param id
     * @return boolean
     */
    Boolean deleteByCondition(Long id);
}
