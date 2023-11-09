package com.ruoyi.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.admin.domain.dto.InvoiceDto;
import com.ruoyi.admin.domain.vo.InvoiceVo;
import com.ruoyi.admin.entity.InvoiceEntity;

import java.util.List;

/**
 * @author gy
 * @date 2023-06-08 9:20
 */
public interface InvoiceService extends IService<InvoiceEntity> {

    /**
     * 新增开票信息后续计算
     * @param invoiceDto 保存的开票dto(带项目id和合同id)
     */
    String saveAndCaculate(InvoiceDto invoiceDto) throws Exception;

    /**
     * 修改开票信息后续计算
     * @param invoiceDto 修改的开票dto(根据id修改)
     */
    String updateAndCaculate(InvoiceDto invoiceDto) throws Exception;

    /**
     * 删除开票信息后续计算(根据id逻辑删除)
     * @param id 需要逻辑删除的开票记录id
     */
    String removeAndCaculate(Long id) throws Exception;

    /**
     *  根据合同id查询时带上项目编号
     */
    List<InvoiceVo> seleteInvoiceWithIdentifier(Long contractId);
}
