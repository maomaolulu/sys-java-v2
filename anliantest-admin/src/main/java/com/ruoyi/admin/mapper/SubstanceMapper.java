package com.ruoyi.admin.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ruoyi.admin.domain.vo.SubstanceVo;
import com.ruoyi.admin.entity.SubstanceEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author ZhuYiCheng
 * @date 2023/4/24 10:51
 */
@Mapper
public interface SubstanceMapper extends BaseMapper<SubstanceEntity> {

//    @Select("select t2.id ,t2.name,t2.s_type,t1.qualification,t1.lab_source from al_substance_detection t1  left join al_substance t2 on " +
//            "t1.substance_id=t2.id left join lab_main_data t3 on t1.main_data_id = t3.id where t2.s_dept=1 and t1.mark_num=1 and t1.lab_source='上海量远'")
//    List<SubstanceVo> getSubstanceList();

    @Select("SELECT s.id,s.name,s.s_type,sd.qualification,sd.lab_source \n" +
            "FROM al_substance s \n" +
            "LEFT JOIN \n" +
            "(SELECT substance_id as id ,name,qualification,lab_source FROM al_substance_detection \n" +
            " WHERE lab_source = '上海量远') as sd \n" +
            "on s.id = sd.id")
    List<SubstanceVo> getSubstanceList();


    @Select("SELECT s.id,s.name,s.s_type,sd.qualification,sd.lab_source \n" +
            "FROM al_substance s \n" +
            "LEFT JOIN \n" +
            "(SELECT substance_id as id ,name,qualification,lab_source FROM al_substance_detection\n" +
            " WHERE lab_source = '上海量远') as sd \n" +
            "on s.id = sd.id \n" +
            "${ew.customSqlSegment}")
    List<SubstanceVo> getById(@Param(Constants.WRAPPER) QueryWrapper<SubstanceVo> queryWrapper);

    List<SubstanceEntity> getAllAndList();
}
