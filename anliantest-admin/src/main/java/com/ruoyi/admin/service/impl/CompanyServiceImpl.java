package com.ruoyi.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.admin.domain.dto.CompanyDto;
import com.ruoyi.admin.domain.vo.CompanyBasicVo;
import com.ruoyi.admin.domain.vo.CompanyContactVo;
import com.ruoyi.admin.domain.vo.CompanyVo;
import com.ruoyi.admin.entity.CompanyAddressEntity;
import com.ruoyi.admin.entity.CompanyContactEntity;
import com.ruoyi.admin.entity.CompanyEntity;
import com.ruoyi.admin.mapper.CompanyAddressMapper;
import com.ruoyi.admin.mapper.CompanyContactMapper;
import com.ruoyi.admin.mapper.CompanyMapper;
import com.ruoyi.admin.domain.vo.AssignmentVo;
import com.ruoyi.admin.service.CompanyAddressService;
import com.ruoyi.admin.service.CompanyContactService;
import com.ruoyi.admin.service.CompanyService;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.pageUtil;
import com.ruoyi.system.login.utils.ShiroUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


/**
 * @author ZhuYiCheng
 * @date 2023/3/25 15:54
 */
@Service
public class CompanyServiceImpl extends ServiceImpl<CompanyMapper, CompanyEntity> implements CompanyService {

    @Autowired
    private CompanyMapper companyMapper;

    @Autowired
    private CompanyAddressMapper companyAddressMapper;

    @Autowired
    private CompanyContactMapper companyContactMapper;

    @Autowired
    private CompanyContactService companyContactService;

    @Autowired
    private CompanyAddressService companyAddressService;


    /**
     * 校验信用社代码
     */
    @Override
    public Boolean checkCode(String code) {

        QueryWrapper<CompanyEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(code), "code", code);
        CompanyEntity company = companyMapper.selectOne(queryWrapper);

        return company != null;
    }

    /**
     * 新增客户
     */
    @Override
    @Transactional
    public Boolean addCompany(CompanyVo companyVo) {
        //新增客户基本信息
        CompanyEntity companyEntity = companyVo.getCompanyEntity();
        Boolean b1 = this.insertCompany(companyEntity);
        //新增受检地址
        String code = companyVo.getCompanyEntity().getCode();
        QueryWrapper<CompanyEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(code), "code", code);
        CompanyEntity companyEntity1 = companyMapper.selectOne(queryWrapper);

        List<CompanyAddressEntity> addressList = companyVo.getAddressEntityList();
        for (CompanyAddressEntity addressEntity : addressList) {
            addressEntity.setCompanyId(companyEntity1.getId());
            addressEntity.setCompany(companyEntity1.getCompany());
        }
        Boolean b2 = companyAddressService.saveBatch(addressList);

        //新增联系人信息
        List<CompanyContactEntity> contactList = companyVo.getContactEntityList();
        for (CompanyContactEntity companyContact : contactList) {
            companyContact.setCompanyId(companyEntity1.getId());
            companyContact.setCompany(companyEntity1.getCompany());
        }
        Boolean b3 = companyContactService.saveBatch(contactList);

        return b1 && b2 && b3;
    }

    /**
     * 新增客户基本信息
     */
    @Override
    public Boolean insertCompany(CompanyEntity company) {
        //填入时间
        Date nowDate = DateUtils.getNowDate();
        company.setCreateTime(nowDate);
        company.setUpdateTime(nowDate);
        //填入录入人和更新人
        company.setUserId(Math.toIntExact(ShiroUtils.getUserId()));
        String nickName = ShiroUtils.getUserName();
        company.setUsername(nickName);
        company.setUpdateBy(nickName);
        //填入数据所属公司=录入员工所属公司
        company.setDataBelong(ShiroUtils.getCompanyName());
        //插入数据
        int result = companyMapper.insert(company);
        return result > 0;
    }

    /**
     * 客户列表查询
     */
    @Override
    public List<CompanyEntity> getCompanyList(CompanyDto companyDto) {

        String company = companyDto.getCompany();
        String code = companyDto.getCode();
        String belong = companyDto.getBelong();
        Integer belongId = companyDto.getBelongId();
        Integer type = companyDto.getType();
        Integer state = companyDto.getState();
        String companyScale = companyDto.getCompanyScale();


        QueryWrapper<CompanyEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(company), "company", company);
        queryWrapper.like(StringUtils.isNotBlank(code), "code", code);
        queryWrapper.like(StringUtils.isNotBlank(belong), "belong", belong);
        queryWrapper.eq(belongId != null, "belong_id", belongId);
        queryWrapper.eq(type != null, "type", type);
        queryWrapper.eq(state != null, "state", state);
        queryWrapper.like(StringUtils.isNotBlank(companyScale), "company_scale", companyScale);
        queryWrapper.orderByDesc("update_time");

        pageUtil.startPage();
        return companyMapper.selectList(queryWrapper);
    }

    /**
     * 导出客户列表查询
     */
    @Override
    public List<CompanyEntity> getCompanyExportList(Map<String, Object> params) {
        String company = (String) params.get("company");
        String code = (String) params.get("code");
        String belong = (String) params.get("belong");
        String type = (String) params.get("type");
        String state = (String) params.get("state");
        String companyScale = (String) params.get("companyScale");

        QueryWrapper<CompanyEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(company), "company", company);
        queryWrapper.like(StringUtils.isNotBlank(code), "code", code);
        queryWrapper.like(StringUtils.isNotBlank(belong), "belong", belong);
        queryWrapper.eq(StringUtils.isNotBlank(type), "type", type);
        queryWrapper.eq(StringUtils.isNotBlank(state), "state", state);
        queryWrapper.like(StringUtils.isNotBlank(companyScale), "company_scale", companyScale);
        queryWrapper.orderByDesc("update_time");

        return companyMapper.selectList(queryWrapper);
    }

    /**
     * 获取“我”的客户列表
     *
     * @param userId
     * @return
     */
    @Override
    public List<CompanyVo> getMyCompany(Long userId) {

        //获取归属人为当前用户的客户基本信息
        List<CompanyEntity> companyEntityList = this.list(new QueryWrapper<CompanyEntity>()
                .eq(userId != null, "belong_id", userId)
                .orderByDesc("create_time"));
        List<Long> companyIdList = new ArrayList<>();
        for (CompanyEntity companyEntity : companyEntityList) {
            Long id = companyEntity.getId();
            companyIdList.add(id);
        }
        //获取客户相关办公地址(受检地址)信息
        List<CompanyAddressEntity> companyAddressEntityList = companyAddressService
                .list(new QueryWrapper<CompanyAddressEntity>()
                        .in(!companyIdList.isEmpty(), "company_id", companyIdList));
        //获取客户相关联系人信息
        List<CompanyContactEntity> companyContactEntityList = companyContactService
                .list(new QueryWrapper<CompanyContactEntity>()
                        .in(!companyIdList.isEmpty(), "company_id", companyIdList));

        List<CompanyVo> companyVoList = new ArrayList<>();
        for (CompanyEntity companyEntity : companyEntityList) {
            CompanyVo companyVo = new CompanyVo();
            companyVo.setCompanyEntity(companyEntity);
            Long companyEntityId = companyEntity.getId();
            //过滤初该公司的办公地址
            List<CompanyAddressEntity> companyAddressEntityList1 = companyAddressEntityList
                    .stream().filter(companyAddressEntity -> companyAddressEntity.getCompanyId()
                            .equals(companyEntityId)).collect(Collectors.toList());
            companyVo.setAddressEntityList(companyAddressEntityList1);
            //过滤该公司的联系人
            List<CompanyContactEntity> companyContactEntityList1 = companyContactEntityList
                    .stream().filter(companyContactEntity -> companyContactEntity.getCompanyId()
                            .equals(companyEntityId)).collect(Collectors.toList());
            companyVo.setContactEntityList(companyContactEntityList1);

            companyVoList.add(companyVo);
        }
        return companyVoList;
    }

    /**
     * 获取客户详情
     */
    @Override
    public CompanyVo getDetail(Long id) {
        CompanyEntity companyEntity = companyMapper.selectById(id);
        QueryWrapper<CompanyAddressEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(id != null, "company_id", id);
        List<CompanyAddressEntity> addressEntityList = companyAddressMapper.selectList(queryWrapper);
        QueryWrapper<CompanyContactEntity> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq(id != null, "company_id", id);
        List<CompanyContactEntity> contactEntityList = companyContactMapper.selectList(queryWrapper1);
        CompanyVo companyVo = new CompanyVo();
        companyVo.setCompanyEntity(companyEntity);
        companyVo.setAddressEntityList(addressEntityList);
        companyVo.setContactEntityList(contactEntityList);
        return companyVo;
    }


    /**
     * 修改客户基本信息
     */
    @Override
    @Transactional
    public Boolean updateCompany(CompanyBasicVo companyBasicVo) {
        CompanyEntity companyEntity = companyBasicVo.getCompanyEntity();
        companyEntity.setUpdateTime(DateUtils.getNowDate());
        companyEntity.setUpdateBy(ShiroUtils.getUserName());

        int i = companyMapper.updateById(companyEntity);
        List<CompanyAddressEntity> addressEntityList = companyBasicVo.getAddressEntityList();
        //删除原来受检地址
        companyAddressService.remove(new QueryWrapper<CompanyAddressEntity>().eq("company_id", companyEntity.getId()));
        //插入新受检地址
        boolean b = companyAddressService.saveBatch(addressEntityList);

        return (i == 1 && b);
    }

    /**
     * 修改联系人信息
     */
    @Override
    @Transactional
    public Boolean updateContact(CompanyContactVo companyContactVo) {
        List<CompanyContactEntity> contactEntityList = companyContactVo.getContactEntityList();
        //删除原来的联系人
        companyContactService.remove(new QueryWrapper<CompanyContactEntity>().eq("company_id", contactEntityList.get(0).getCompanyId()));
        //插入新的联系人
        return companyContactService.saveBatch(contactEntityList);

    }

    /**
     * 领取客户资源
     */
    @Override
    @Transactional
    public Boolean getCompany(AssignmentVo assignmentVo) {
        List<Long> idList = assignmentVo.getIds();
        Long userId = assignmentVo.getUserId();
        String username = assignmentVo.getUsername();
        //Date nowDate = DateUtils.getNowDate();
        List<CompanyEntity> companyEntityList = this.listByIds(idList);
        for (CompanyEntity companyEntity : companyEntityList) {
            Integer state = companyEntity.getState();
            if (state != 0) {
                return false;
            }
            companyEntity.setState(1);
            companyEntity.setBelong(username);
            companyEntity.setBelongId(Math.toIntExact(userId));
            companyEntity.setUpdateBy(username);
            //客户资源的领取和释放、分配不会影响客户信息的更新时间
            //companyEntity.setUpdateTime(nowDate);
        }
        return this.updateBatchById(companyEntityList);
    }

    /**
     * 释放客户资源
     */
    @Override
    public Boolean releaseCompany(AssignmentVo assignmentVo) {
        List<Long> idList = assignmentVo.getIds();
        //Long userId = assignmentVo.getUserId();
        String username = assignmentVo.getUsername();
        //Date nowDate = DateUtils.getNowDate();
        List<CompanyEntity> companyEntityList = this.listByIds(idList);
        for (CompanyEntity companyEntity : companyEntityList) {
            companyEntity.setState(0);
            companyEntity.setBelongId(0);
            companyEntity.setBelong("");
            companyEntity.setUpdateBy(username);
            //客户资源的领取和释放、分配不会影响客户信息的更新时间
            //companyEntity.setUpdateTime(nowDate);
        }
        return this.updateBatchById(companyEntityList);
    }

    /**
     * 分配客户资源
     */
    @Override
    public Boolean allotCompany(AssignmentVo assignmentVo) {
        List<Long> idList = assignmentVo.getIds();
        Long userId = assignmentVo.getUserId();
        String username = assignmentVo.getUsername();
        //Date nowDate = DateUtils.getNowDate();
        String updateUser = ShiroUtils.getUserName();
        List<CompanyEntity> companyEntityList = this.listByIds(idList);
        for (CompanyEntity companyEntity : companyEntityList) {
            companyEntity.setState(1);
            companyEntity.setBelong(username);
            companyEntity.setBelongId(Math.toIntExact(userId));
            companyEntity.setUpdateBy(updateUser);
            //客户资源的领取和释放、分配不会影响客户信息的更新时间
            //companyEntity.setUpdateTime(nowDate);
        }
        return this.updateBatchById(companyEntityList);
    }


}
