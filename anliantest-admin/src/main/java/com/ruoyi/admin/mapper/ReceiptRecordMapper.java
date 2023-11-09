package com.ruoyi.admin.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ruoyi.admin.entity.ReceiptRecordEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author ZhuYiCheng
 * @date 2023/6/8 15:32
 */
@Mapper
public interface ReceiptRecordMapper extends BaseMapper<ReceiptRecordEntity> {

    @Select("select r.id,p.identifier as project_identifier,r.project_id,r.contract_id,r.receipt_time,r.receipt_money" +
            ",r.payment_method,r.remark,r.create_by,r.create_time,r.update_by,r.update_time \n" +
            "from ly_receipt_record as r LEFT JOIN ly_project as p on r.project_id =p.id \n" +
            "${ew.customSqlSegment}")
    List<ReceiptRecordEntity> getList(@Param(Constants.WRAPPER) QueryWrapper<ReceiptRecordEntity> queryWrapper);

}
