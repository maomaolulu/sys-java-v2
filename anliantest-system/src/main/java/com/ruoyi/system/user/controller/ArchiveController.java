package com.ruoyi.system.user.controller;

import com.ruoyi.common.annotation.OperateLog;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.R;
import com.ruoyi.system.file.domain.SysAttachment;
import com.ruoyi.system.file.service.SysAttachmentService;
import com.ruoyi.system.file.service.impl.SysAttachmentServiceImpl;
import com.ruoyi.system.user.entity.ArchiveEntity;
import com.ruoyi.system.user.service.ArchiveService;
import io.minio.errors.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

/**
 * @author ZhuYiCheng
 * @date 2023/5/17 18:48
 */
@RestController
@Api("用户档案管理")
@RequestMapping("/sys/archive")
public class ArchiveController {

    private final ArchiveService archiveService;

    @Autowired
    public ArchiveController(ArchiveService archiveService, SysAttachmentService sysAttachmentService) {
        this.archiveService = archiveService;
    }

    /**
     * 新增用户档案
     *
     * @param archiveEntity
     * @return
     */
    @ApiOperation("新增用户档案")
    @OperateLog(title = "新增用户档案")
    @PostMapping("/addArchive")
    public R addArchive(@RequestBody ArchiveEntity archiveEntity) {

        Boolean b = archiveService.addArchive(archiveEntity);

        return b ? R.ok() : R.error();
    }

    /**
     * 批量新增用户档案
     *
     * @param archiveEntityList
     * @return
     */
    @ApiOperation("批量新增用户档案")
    @OperateLog(title = "批量新增用户档案")
    @PostMapping("/addArchiveBatch")
    public R addFileBatch(@RequestBody List<ArchiveEntity> archiveEntityList) {

        Boolean b = archiveService.addFileBatch(archiveEntityList);
        return b ? R.ok() : R.error();
    }

    /**
     * 根据档案类型，userId获取用户档案列表
     *
     * @return
     */
    @ApiOperation("获取用户档案列表")
    @GetMapping("/list")
    public R getArchiveList(@RequestParam("type") Integer type, @RequestParam("userId") Long userId) throws ServerException, InvalidBucketNameException, InsufficientDataException, ErrorResponseException, InvalidExpiresRangeException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {

        List<ArchiveEntity> archiveEntityList = archiveService.getArchiveList(type, userId);

        return R.ok("查询成功", archiveEntityList);
    }

    /**
     * 修改用户档案信息
     *
     * @return
     */
    @ApiOperation("修改用户档案信息")
    @OperateLog(title = "修改用户档案信息")
    @PutMapping("/updateArchive")
    public R updateArchive(@RequestBody ArchiveEntity archiveEntity) {

//        Boolean b = archiveService.updateArchive(archiveEntityList);
        Boolean b = archiveService.updateArchive(archiveEntity);

        return b ? R.ok() : R.error();
    }


    /**
     * 逻辑删除用户档案信息
     * @param archiveEntity
     * @return
     */
    @ApiOperation("逻辑删除用户档案信息")
    @OperateLog(title = "逻辑删除用户档案信息")
    @PutMapping("/deleteArchive")
    public R deleteArchive(@RequestBody ArchiveEntity archiveEntity) {
        Long id = archiveEntity.getId();

        Boolean b = archiveService.deleteArchive(id);

        return b ? R.ok() : R.error();
    }


}
