package com.ruoyi.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.admin.domain.dto.ProjectReceiptDto;
import com.ruoyi.admin.entity.ReceiptRecordEntity;

import java.util.List;

/**
 * @author ZhuYiCheng
 * @date 2023/6/8 15:34
 */
public interface ReceiptRecordService extends IService<ReceiptRecordEntity> {


    /**
     * 新增回款记录
     * @param receiptRecordEntity
     * @return
     */
    String addRecord(ReceiptRecordEntity receiptRecordEntity) throws Exception;

    /**
     * 获取回款记录列表
     * @param receiptDto
     * @return
     */
    List<ReceiptRecordEntity> getList(ProjectReceiptDto receiptDto);

    /**
     * 修改回款记录
     * @param receiptRecordEntity
     * @return
     */
    String updateRecord(ReceiptRecordEntity receiptRecordEntity) throws Exception;

    /**
     * 逻辑删除回款记录
     * @param id
     * @return
     */
    String deleteRecord(Long id) throws Exception;
}
