package com.ruoyi.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.admin.domain.dto.ProcessFlowDto;
import com.ruoyi.admin.entity.ProcessFlowEntity;
import org.apache.ibatis.annotations.Mapper;


/**
 * @author gy
 * @date 2023-06-12 14:04
 */
@Mapper
public interface ProcessFlowMapper extends BaseMapper<ProcessFlowEntity> {

    /**
     * 查询树形结构List
     * @param processFlowDto 查询条件
     * @return List<ProcessFlowVo>
     */
    Object selectWithPage(ProcessFlowDto processFlowDto);

    /**
     * 保存工艺流程并完成
     * @param processFlowDto 新增内容
     * @return Boolean
     */
    Boolean SaveAndModify(ProcessFlowDto processFlowDto);

    /**
     * 更新一个或多个工艺流程
     * @param processFlowDto 更新条件
     * @return Boolean
     */
    Boolean updateOneOrMore(ProcessFlowDto processFlowDto);

    /**
     * 删除一个或多个工艺流程
     * @param id 需要删除的id
     * @return Boolean
     */
    Boolean deleteOnOrMore(Long id);
}
