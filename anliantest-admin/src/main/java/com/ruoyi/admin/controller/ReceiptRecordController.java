package com.ruoyi.admin.controller;

import com.ruoyi.admin.domain.dto.ProjectReceiptDto;
import com.ruoyi.admin.entity.ReceiptRecordEntity;
import com.ruoyi.admin.service.ReceiptRecordService;
import com.ruoyi.common.annotation.OperateLog;
import com.ruoyi.common.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ZhuYiCheng
 * @date 2023/6/8 15:37
 */
@RestController
@Api(tags = "回款管理")
@RequestMapping("/liangyuan/receipt")
public class ReceiptRecordController {


    @Autowired
    private ReceiptRecordService receiptRecordService;


    /**
     * 新增回款记录
     *
     * @return
     */
    @PostMapping("/addRecord")
    @OperateLog(title = "新增回款记录")
    @ApiOperation("新增回款记录")
    public R addRecord(@RequestBody ReceiptRecordEntity receiptRecordEntity) throws Exception {
        String result = receiptRecordService.addRecord(receiptRecordEntity);
        return R.ok(result);
    }

    /**
     * 获取回款记录列表
     *
     * @return
     */
    @ApiOperation("获取回款记录列表")
    @GetMapping("/list")
    public R getList(ProjectReceiptDto receiptDto) {

        List<ReceiptRecordEntity> recordEntityList = receiptRecordService.getList(receiptDto);
        return R.ok("查询成功",recordEntityList);
    }


    /**
     * 获取单个回款记录详情
     *
     * @return
     */
    @ApiOperation("获取单个回款记录详情")
    @GetMapping("/getDetail")
    public R getDetail(Long id) {

        ReceiptRecordEntity receiptRecordEntity = receiptRecordService.getById(id);
        return R.ok("查询成功",receiptRecordEntity);
    }


    /**
     * 修改回款记录
     *
     * @return
     */
    @PutMapping("/updateRecord")
    @OperateLog(title = "修改回款记录")
    @ApiOperation("修改回款记录")
    public R updateRecord(@RequestBody ReceiptRecordEntity receiptRecordEntity) throws Exception {
        String result = receiptRecordService.updateRecord(receiptRecordEntity);
        return R.ok(result);
    }


    /**
     * 逻辑删除回款记录
     *
     * @return
     */
    @PutMapping("/deleteRecord")
    @OperateLog(title = "逻辑删除回款记录")
    @ApiOperation("逻辑删除回款记录")
    public R deleteRecord(@RequestBody ReceiptRecordEntity recordEntity) throws Exception {
        Long id = recordEntity.getId();
        String result = receiptRecordService.deleteRecord(id);
        return R.ok(result);
    }

}
