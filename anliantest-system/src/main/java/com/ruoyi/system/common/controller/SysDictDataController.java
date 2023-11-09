package com.ruoyi.system.common.controller;

import cn.hutool.core.collection.CollUtil;
import com.ruoyi.common.annotation.OperateLog;
import com.ruoyi.common.constant.AttachmentConstants;
import com.ruoyi.common.enums.DeleteFlag;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.R;
import com.ruoyi.system.common.entity.SysDictData;
import com.ruoyi.system.common.service.SysDictDataService;
import com.ruoyi.system.file.domain.Res;
import com.ruoyi.system.file.domain.SysAttachment;
import com.ruoyi.system.file.domain.SysStaticImg;
import com.ruoyi.system.file.service.SysAttachmentService;
import com.ruoyi.system.file.service.SysStaticImgService;
import io.minio.*;
import io.minio.http.Method;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 文件服务
 *
 * @menu 文件服务
 */
@Slf4j
@RestController
@RequestMapping("/dict_data")
public class SysDictDataController {
    @Resource
    private SysDictDataService sysDictDataService;

    @GetMapping
    public R list(){
        List<SysDictData> list = sysDictDataService.list();
        if(CollUtil.isNotEmpty(list)){
            Map<String, List<SysDictData>> collect = list.stream().distinct().collect(Collectors.groupingBy(SysDictData::getDictType));

            return R.data(collect);

        }
        return R.ok();
    }
}