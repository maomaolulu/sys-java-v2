package com.ruoyi.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.admin.domain.vo.InvoiceVo;
import com.ruoyi.admin.entity.InvoiceEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author gy
 * @date 2023-06-08 9:19
 */
@Mapper
public interface InvoiceMapper extends BaseMapper<InvoiceEntity> {
    Boolean saveAndCaculate(InvoiceEntity invoiceEntity);

    Boolean updateAndCaculate(InvoiceEntity invoiceEntity);

    Boolean removeAndCaculate(Long id);

    @Select("SELECT ir.*,p.identifier project_identifier FROM `ly_invoice_record` ir\n" +
            "LEFT JOIN `ly_project` p ON p.id = ir.project_id\n" +
            "WHERE ir.delete_flag = 0 AND ir.contract_id = #{contractId}")
    List<InvoiceVo> seleteInvoiceWithIdentifier(Long contractId);
}
