package com.ruoyi.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.admin.domain.dto.CompanyDto;
import com.ruoyi.admin.domain.vo.CompanyBasicVo;
import com.ruoyi.admin.domain.vo.CompanyContactVo;
import com.ruoyi.admin.domain.vo.CompanyVo;
import com.ruoyi.admin.entity.CompanyEntity;
import com.ruoyi.admin.domain.vo.AssignmentVo;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * @author ZhuYiCheng
 * @date 2023/3/25 15:53
 */
public interface CompanyService extends IService<CompanyEntity> {


    /**
     * 校验信用社代码
     */
    Boolean checkCode(String code);

    /**
     * 新增客户
     */
    Boolean addCompany(CompanyVo companyVo) throws ParseException;

    /**
     * 新增客户基本信息
     */
    Boolean insertCompany(CompanyEntity company);

    /**
     * 客户列表查询
     */
    List<CompanyEntity> getCompanyList(CompanyDto companyDto);

    /**
     * 导出客户列表查询
     */
    List<CompanyEntity> getCompanyExportList(Map<String, Object> params);

    /**
     * 获取“我”的客户列表
     * @param userId
     * @return
     */
    List<CompanyVo> getMyCompany(Long userId);

    /**
     * 获取客户详情
     */
    CompanyVo getDetail(Long id);


    /**
     * 修改客户基本信息
     */
    Boolean updateCompany(CompanyBasicVo companyBasicVo);

    /**
     * 修改联系人信息
     */
    Boolean updateContact(CompanyContactVo companyContactVo);

    /**
     * 领取客户资源
     */
    Boolean getCompany(AssignmentVo assignmentVo);

    /**
     * 释放客户资源
     */
    Boolean releaseCompany(AssignmentVo assignmentVo);

    /**
     *分配客户资源
     */
    Boolean allotCompany(AssignmentVo assignmentVo);



}
