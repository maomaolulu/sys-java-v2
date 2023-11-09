package com.ruoyi.system.file.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.system.file.domain.SysStaticImg;
import com.ruoyi.system.file.mapper.SysStaticImgMapper;
import com.ruoyi.system.file.service.SysStaticImgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zx
 * @date 2022/1/7 17:52
 */
@Service
public class SysStaticImgServiceImpl implements SysStaticImgService {
    private final SysStaticImgMapper sysStaticImgMapper;
    @Autowired
    public SysStaticImgServiceImpl(SysStaticImgMapper sysStaticImgMapper) {
        this.sysStaticImgMapper = sysStaticImgMapper;
    }

    @Override
    public int insert(SysStaticImg sysStaticImg) {
        return sysStaticImgMapper.insert(sysStaticImg);
    }

    /**
     * 获取静态文件列表
     *
     * @return
     */
    @Override
    public List<SysStaticImg> getList() {
        return sysStaticImgMapper.selectList(null);
    }

    /**
     * 删除静态文件
     *
     * @param bucketName
     * @param path
     */
    @Override
    public void delete(String bucketName, String path) {
        QueryWrapper<SysStaticImg> wrapper = new QueryWrapper<>();
        wrapper.eq("bucketName", bucketName);
        wrapper.eq("path", path);
        sysStaticImgMapper.delete(wrapper);
    }
}
