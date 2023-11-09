package com.ruoyi.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.admin.domain.dto.ProjectWorkshopFromDto;
import com.ruoyi.admin.entity.QuotationEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author ZhuYiCheng
 * @date 2023/4/20 10:33
 */
@Mapper
public interface QuotationMapper extends BaseMapper<QuotationEntity> {
    /**
     * 最终车间岗位
     * @param projectId
     * @return
     */
    @Select("SELECT pw.`name` building, pw2.`name` workshop ,pw3.`name` post  FROM `ly_project_workshop` pw\n" +
            "left join `ly_project_workshop` pw2 on pw.id=pw2.pid\n" +
            "left join `ly_project_workshop` pw3 on pw2.id=pw3.pid\n" +
            "where pw.type=0 and  pw.project_id=#{projectId  }\n" +
            "ORDER BY pw.id,pw2.id,pw3.id  ")
     List<ProjectWorkshopFromDto> workshopList(Long projectId);
}
