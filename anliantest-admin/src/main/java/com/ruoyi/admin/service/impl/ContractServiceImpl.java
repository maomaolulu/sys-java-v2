package com.ruoyi.admin.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.admin.domain.dto.ContractDto;
import com.ruoyi.admin.domain.vo.AssignmentVo;
import com.ruoyi.admin.domain.vo.ContractOaVo;
import com.ruoyi.admin.domain.vo.ContractVo;
import com.ruoyi.admin.domain.vo.ProjectVo;
import com.ruoyi.admin.entity.*;
import com.ruoyi.admin.mapper.*;
import com.ruoyi.admin.service.*;
import com.ruoyi.common.constant.AttachmentConstants;
import com.ruoyi.common.constant.BucketNumConstants;
import com.ruoyi.common.enums.DeleteFlag;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.R;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.pageUtil;
import com.ruoyi.system.common.CodeGenerateService;
import com.ruoyi.system.common.CommonConstants;
import com.ruoyi.system.common.RedisService;
import com.ruoyi.system.file.domain.SysAttachment;
import com.ruoyi.system.file.service.SysAttachmentService;
import com.ruoyi.system.login.utils.ShiroUtils;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.errors.*;
import io.minio.http.Method;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author ZhuYiCheng
 * @date 2023/3/31 15:24
 */
@Service
@Slf4j
public class ContractServiceImpl extends ServiceImpl<ContractMapper, ContractEntity> implements ContractService {

    @Autowired
    private ContractMapper contractMapper;

    @Autowired
    private ProjectMapper projectMapper;

    @Autowired
    private ProjectDateMapper projectDateMapper;

    @Autowired
    private ProjectDateService projectDateService;

    @Autowired
    private ProjectAmountService projectAmountService;

    @Autowired
    private ProjectAmountMapper projectAmountMapper;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private QuotationService quotationService;

    @Autowired
    private QuotationMapper quotationMapper;

    @Autowired
    private CodeGenerateService codeGenerateService;

    @Autowired
    private SysAttachmentService sysAttachmentService;

    @Autowired
    private RedisService redisService;
    //oa接口 人员
    @Value("${interface_out.oa.user_url}")
    private String user_url;
    //oa接口 合同审批
    @Value("${interface_out.oa.contract_approval}")
    private String contract_approval;
    //oa接口 合同撤销
    @Value("${interface_out.oa.contract_cancellation}")
    private String contract_cancellation;
    //MD5密码
    @Value("${interface_out.oa.interface_password}")
    private String interface_password;


    /**
     * 新增合同
     */
    @Override
    @Transactional
    public R addContract(ContractVo contractVo) {
        ContractEntity contract = contractVo.getContractEntity();
        //拿前端传的项目信息列表
        List<ProjectEntity> projectEntityList = contractVo.getProjectEntityList();

        Long userId = ShiroUtils.getUserId();
        String userName = ShiroUtils.getUserName();

        contract.setCompanyId(contract.getEntrustCompanyId());
        contract.setCompany(contract.getEntrustCompany());
        contract.setUserid(Math.toIntExact(userId));
        contract.setSalesmenid(Math.toIntExact(userId));
        if (projectEntityList != null) {
            contract.setProjectQuantity(projectEntityList.size());
        }

        //放入userName
        contract.setUsername(userName);
        contract.setSalesmen(userName);
        Date nowDate = DateUtils.getNowDate();
        contract.setCreatetime(nowDate);
        contract.setUpdatetime(nowDate);

        BigDecimal totalMoney = new BigDecimal(0);
        for (ProjectEntity projectEntity : projectEntityList) {
            BigDecimal totalMoney1 = projectEntity.getTotalMoney();
            if (totalMoney1 != null) {
                totalMoney = totalMoney.add(totalMoney1);
            }
        }
        contract.setTotalMoney(totalMoney);
        //存入合同基本信息

        String contractCode = codeGenerateService.getContractCode(contract.getType());
        contract.setIdentifier(contractCode);
        boolean b = this.save(contract);

        //存完合同之后拿到合同id，关联到项目
//        ContractEntity contract1 = contractMapper.selectOne(new QueryWrapper<ContractEntity>().eq("identifier", contract.getIdentifier()));

        //填充项目信息

        for (ProjectEntity projectEntity : projectEntityList) {
            String projectCode = codeGenerateService.getProjectCode(contractCode);
            projectEntity.setIdentifier(projectCode);
            projectEntity.setContractId(contract.getId());
            projectEntity.setContractIdentifier(contract.getIdentifier());
            projectEntity.setCompanyOrder(contract.getCompanyOrder());
            projectEntity.setBusinessSource(contract.getBusinessSource());
            projectEntity.setUserid(Math.toIntExact(userId));
            projectEntity.setSalesmenid(userId);
            projectEntity.setUsername(userName);
            projectEntity.setSalesmen(userName);
            projectEntity.setCreatetime(nowDate);
            projectEntity.setUpdatetime(nowDate);
        }
        //批量存入项目信息
        boolean b1 = projectService.saveBatch(projectEntityList);

        //项目存完之后拿出来项目id,关联项目时间表(ly_project_date),项目金额表(ly_project_amount),报价单基础表(ly_quotation)
        List<ProjectEntity> projectEntityList1 = projectMapper.selectList(new QueryWrapper<ProjectEntity>().eq("contract_id", contract.getId()));

        List<ProjectDateEntity> projectDateEntityList = new ArrayList<>();
        List<ProjectAmountEntity> projectAmountEntityList = new ArrayList<>();
        List<QuotationEntity> quotationEntityList = new ArrayList<>();

        for (ProjectEntity projectEntity : projectEntityList1) {
            //设置projectDateEntity信息
            ProjectDateEntity projectDateEntity = new ProjectDateEntity();
            projectDateEntity.setProjectId(projectEntity.getId());
            projectDateEntity.setEntrustDate(nowDate);

            projectDateEntityList.add(projectDateEntity);
            //设置ProjectAmountEntity信息
            ProjectAmountEntity projectAmountEntity = new ProjectAmountEntity();
            projectAmountEntity.setProjectId(projectEntity.getId());
            projectAmountEntity.setContractId(projectEntity.getContractId());
            projectAmountEntity.setTotalMoney(projectEntity.getTotalMoney());
            projectAmountEntity.setCommission(projectEntity.getCommission());
            projectAmountEntity.setEvaluationFee(projectEntity.getEvaluationFee());
            projectAmountEntity.setSubprojectFee(projectEntity.getSubprojectFee());
            projectAmountEntity.setServiceCharge(projectEntity.getServiceCharge());
            projectAmountEntity.setOtherExpenses(projectEntity.getOtherExpenses());

            projectAmountEntityList.add(projectAmountEntity);
            //合同关联报价单,项目关联的报价单,存合同id,项目id
            QuotationEntity quotationEntity = new QuotationEntity();
            quotationEntity.setContractId(projectEntity.getContractId());
            quotationEntity.setId(projectEntity.getQuotationId());
            quotationEntity.setCode(projectEntity.getQuotationCode());
            quotationEntity.setProjectId(projectEntity.getId());

            quotationEntityList.add(quotationEntity);
        }

        //批量存ProjectDateEntity
        boolean b2 = projectDateService.saveBatch(projectDateEntityList);

        //批量存ProjectAmountEntity
        boolean b3 = projectAmountService.saveBatch(projectAmountEntityList);

        //批量修改报价单信息,存合同id,项目id
        boolean b4 = quotationService.updateBatchById(quotationEntityList);

        if (contractVo.getType() != null && contractVo.getType() == 1) {
            R r = contractExamine(contract.getId());
            Object o = r.get("code");
            if ("200".equals(o)) {
                return R.ok("创建并提交审核成功");
            } else {
                return R.ok("创建成功，提交审核失败,请在详情重新提交评审");
            }
        }

        return R.ok("创建成功");
    }


    /**
     * 合同列表查询
     */
    @Override
    public List<ContractEntity> getContractList(ContractDto contractDto) {

        Long salesmenid = contractDto.getSalesmenid();
        String entrustCompany = contractDto.getEntrustCompany();
        String company = contractDto.getCompany();
        String identifier = contractDto.getIdentifier();
        Integer contractStatus = contractDto.getContractStatus();
        Integer reviewStatus = contractDto.getReviewStatus();
        Integer performStatus = contractDto.getPerformStatus();
        String startSignDate = contractDto.getStartSignDate();
        String endSignDate = contractDto.getEndSignDate();
        QueryWrapper<ContractEntity> queryWrapper = new QueryWrapper<ContractEntity>()
                .eq(!ShiroUtils.isAdmin() && salesmenid != null, "c.salesmenid", salesmenid)
                .like(StringUtils.isNotBlank(entrustCompany), "c.entrust_company", entrustCompany)
                .like(StringUtils.isNotBlank(company), "c.company", company)
                .like(identifier != null, "c.identifier", identifier)
                .eq(contractStatus != null, "c.contract_status", contractStatus)
                .eq(reviewStatus != null, "c.review_status", reviewStatus)
                .eq(performStatus != null, "c.perform_status", performStatus)
                .ge(StringUtils.isNotBlank(startSignDate), "c.sign_date", startSignDate)
                .le(StringUtils.isNotBlank(endSignDate), "c.sign_date", endSignDate)
                .eq("c.del_flag", DeleteFlag.NO.ordinal())
                .orderByDesc("c.createtime");

        pageUtil.startPage();
        List<ContractEntity> contractEntityList = contractMapper.getList(queryWrapper);
        return contractEntityList;
    }

    /**
     * 获取合同详情
     */
    @Override
    public ContractVo getDetail(Long id) {
        ContractEntity contractEntity = contractMapper.selectById(id);
        //根据桶名和合同id获取附件列表
        List<SysAttachment> sysAttachmentList = sysAttachmentService.list(new QueryWrapper<SysAttachment>()
                .in("p_id", id)
                .eq("bucket_name", BucketNumConstants.TYPE_3)
                .eq("temp_id", AttachmentConstants.FOREVER)
                .eq("del_flag", DeleteFlag.NO.ordinal()));
        try {
            //给每一个文件拼preUrl
            for (SysAttachment sysAttachment : sysAttachmentList) {
                String fileUrl = getFileUrl(sysAttachment.getBucketName(), sysAttachment.getPath());
                String preUrl = fileUrl.substring(fileUrl.lastIndexOf("/ly-"));
                sysAttachment.setPreUrl(preUrl);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info(e.getMessage());
        }
        //返回合同文件
        contractEntity.setSysAttachmentList(sysAttachmentList);

        List<ProjectEntity> projectEntityList = projectMapper.selectList(new QueryWrapper<ProjectEntity>()
                .eq("contract_id", id)
                .eq("del_flag", DeleteFlag.NO.ordinal()));
        for (ProjectEntity projectEntity : projectEntityList) {
            ProjectAmountEntity projectAmountEntity = projectAmountMapper.selectOne(new QueryWrapper<ProjectAmountEntity>()
                    .eq("project_id", projectEntity.getId()));
            if (projectAmountEntity != null) {
                projectEntity.setEvaluationFee(projectAmountEntity.getEvaluationFee());
                projectEntity.setCommission(projectAmountEntity.getCommission());
                projectEntity.setSubprojectFee(projectAmountEntity.getSubprojectFee());
                projectEntity.setServiceCharge(projectAmountEntity.getServiceCharge());
                projectEntity.setOtherExpenses(projectAmountEntity.getOtherExpenses());
            }
        }
        List<QuotationEntity> quotationEntityList = quotationMapper.selectList(new QueryWrapper<QuotationEntity>()
                .eq("contract_id", id)
                .eq("del_flag", DeleteFlag.NO.ordinal()));

        Map<Long, String> identifierMap = projectEntityList.stream()
                .collect(Collectors.toMap(ProjectEntity::getId, ProjectEntity::getIdentifier));

        for (QuotationEntity quotationEntity : quotationEntityList) {
            quotationEntity.setProjectIdentifier(identifierMap.get(quotationEntity.getProjectId()));
        }
        ContractVo contractVo = new ContractVo();
        contractVo.setContractEntity(contractEntity);
        contractVo.setProjectEntityList(projectEntityList);
        contractVo.setQuotationEntityList(quotationEntityList);
        return contractVo;
    }

    @Resource
    private MinioClient minioClient;

    private String getFileUrl(String bucketName, String path) throws ServerException, InvalidBucketNameException, InsufficientDataException, ErrorResponseException, InvalidExpiresRangeException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        String url = minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder().bucket(bucketName).object(path).method(Method.GET).expiry(60, TimeUnit.MINUTES).build());
        return url;
    }

    /**
     * 修改合同基本信息
     */
    @Override
    public Boolean updateContract(ContractEntity contractEntity) {
        Date nowDate = DateUtils.getNowDate();
        contractEntity.setUpdatetime(nowDate);
        int update = contractMapper.updateById(contractEntity);
        return update == 1;
    }

    /**
     * 批量移交合同及关联项目
     */
    @Override
    @Transactional
    public Boolean transferContract(AssignmentVo assignmentVo) {
        List<Long> idList = assignmentVo.getIds();

        Date nowDate = DateUtils.getNowDate();
        //修改合同表业务员
        List<ContractEntity> contractEntityList = contractMapper.selectBatchIds(idList);
        for (ContractEntity contractEntity : contractEntityList) {
            contractEntity.setSalesmenid(Math.toIntExact(assignmentVo.getUserId()));
            contractEntity.setSalesmen(assignmentVo.getUsername());
            contractEntity.setUpdatetime(nowDate);
        }
        boolean b = this.updateBatchById(contractEntityList);
        //修改项目表业务员
        List<ProjectEntity> projectEntityList = projectMapper.selectList(new QueryWrapper<ProjectEntity>()
                .in("contract_id", idList)
                .eq("del_flag", DeleteFlag.NO.ordinal()));
        List<Long> projectIdList = new ArrayList<>();
        if (projectEntityList != null && projectEntityList.size() > 0) {
            for (ProjectEntity projectEntity : projectEntityList) {
                //如果项目已完成或已终止，项目业务员不再改变 项目状态(0:未启动，1：已启动，2：已完成，3：已结束 4：终止 5：挂起/暂停)
                Integer status = projectEntity.getStatus();
                if (status >= 2) {
                    continue;
                }
                projectEntity.setSalesmenid(assignmentVo.getUserId());
                projectEntity.setSalesmen(assignmentVo.getUsername());
                projectEntity.setUpdatetime(nowDate);
                //收集符合条件的项目的id
                projectIdList.add(projectEntity.getId());
            }
            boolean b1 = projectService.updateBatchById(projectEntityList);
        }

        //修改报价单表业务员
        List<QuotationEntity> quotationEntityList = quotationMapper.selectList(new QueryWrapper<QuotationEntity>()
                .in(projectIdList.size() > 0, "project_id", projectIdList)
                .eq("del_flag", DeleteFlag.NO.ordinal()));
        if (quotationEntityList != null && quotationEntityList.size() > 0) {
            for (QuotationEntity quotationEntity : quotationEntityList) {
                quotationEntity.setSalesmenId(assignmentVo.getUserId());
                quotationEntity.setSalesmen(assignmentVo.getUsername());
                quotationEntity.setUpdateTime(nowDate);
            }
            boolean b2 = quotationService.updateBatchById(quotationEntityList);
        }

        return b;

    }


    /**
     * 签订合同
     */
    @Override
    @Transactional
    public Boolean signContract(ContractEntity contractEntity) {
        contractEntity.setContractStatus(1);
        contractEntity.setUpdatetime(DateUtils.getNowDate());
        List<ProjectEntity> projectEntityList = projectMapper.selectList(new QueryWrapper<ProjectEntity>()
                .eq("contract_id", contractEntity.getId())
                .eq("del_flag", DeleteFlag.NO.ordinal()));

        List<ProjectDateEntity> projectDateEntityList = new ArrayList<>();
        for (ProjectEntity projectEntity : projectEntityList) {
            //修改项目表合同签订状态和时间
            projectEntity.setContractSignStatus(1);
            projectEntity.setContractSignDate(contractEntity.getSignDate());
            ProjectDateEntity projectDateEntity = projectDateMapper.selectOne(new QueryWrapper<ProjectDateEntity>().eq("project_id", projectEntity.getId()));
            if (projectDateEntity != null) {
                projectDateEntityList.add(projectDateEntity);
            }
        }
        for (ProjectDateEntity projectDateEntity : projectDateEntityList) {
            projectDateEntity.setSignDate(contractEntity.getSignDate());
        }
        if (CollectionUtil.isNotEmpty(projectDateEntityList)) {
            projectDateService.updateBatchById(projectDateEntityList);
        }
        //修改项目表合同签订状态和时间
        boolean b = projectService.updateBatchById(projectEntityList);
        boolean b1 = this.updateById(contractEntity);
        //将档案id存入对应附件表parentId
        Long contractId = contractEntity.getId();
        List<SysAttachment> sysAttachmentList = contractEntity.getSysAttachmentList();
        if (sysAttachmentList != null && sysAttachmentList.size() > 0) {
            for (SysAttachment sysAttachment : sysAttachmentList) {
                sysAttachment.setPId(contractId);
                //临时转有效
                sysAttachment.setTempId(AttachmentConstants.FOREVER);
            }
        }
        boolean b2 = sysAttachmentService.updateBatchById(sysAttachmentList);
        //合同签订之后，不能再新增项目，删除Redis中该合同对应的项目编号键值
        redisService.deleteObject(CommonConstants.CODE_KEY_PACKAGE + contractEntity.getIdentifier());
        return b && b1 && b2;
    }


    /**
     * 终止合同
     */
    @Override
    @Transactional
    public Boolean terminateContract(ContractEntity contractEntity) {
        Long contractEntityId = contractEntity.getId();
        //合同履约状态改为合同终止  履约状态（0待履约,1履约中,2履约完成,3合同终止）
        contractEntity.setPerformStatus(3);
        contractEntity.setTerminateTime(contractEntity.getTerminateTime());
        contractEntity.setUpdatetime(DateUtils.getNowDate());
        //终止合同
        boolean b = this.updateById(contractEntity);

        List<ProjectEntity> projectEntityList = projectMapper.selectList(new QueryWrapper<ProjectEntity>()
                .eq("contract_id", contractEntityId));
        if (projectEntityList != null && projectEntityList.size() > 0) {
            //终止合同关联的项目 项目状态(0:未启动，1：已启动，2：已完成，3：已结束 4：终止 5：挂起/暂停)
            projectEntityList.forEach(projectEntity -> {
                projectEntity.setStatus(4);
                projectEntity.setTerminateReason("合同终止");
            });
            boolean b1 = projectService.updateBatchById(projectEntityList);
        }

        List<QuotationEntity> quotationEntityList = quotationMapper.selectList(new QueryWrapper<QuotationEntity>()
                .eq("contract_id", contractEntityId));
        if (quotationEntityList != null && quotationEntityList.size() > 0) {
            //终止项目关联的报价单
            quotationEntityList.forEach(quotationEntity -> quotationEntity.setStatus(5));
            boolean b2 = quotationService.updateBatchById(quotationEntityList);
        }
        return b;
    }

    /**
     * 删除(逻辑)合同
     */
    @Override
    @Transactional
    public Boolean deleteContract(List<Long> idList) {
        //逻辑删除合同
        boolean b = this.update(new UpdateWrapper<ContractEntity>()
                .set("del_flag", DeleteFlag.YES.ordinal())
                .in("id", idList));

        List<QuotationEntity> quotationEntityList = quotationMapper.selectList(new QueryWrapper<QuotationEntity>()
                .in("contract_id", idList));
        if (quotationEntityList != null && quotationEntityList.size() > 0) {
            //逻辑删除合同关联报价单
            boolean b1 = quotationService.update(new UpdateWrapper<QuotationEntity>()
                    .set("del_flag", DeleteFlag.YES.ordinal())
                    .in("contract_id", idList));
        }

        List<ProjectEntity> projectEntityList = projectMapper.selectList(new QueryWrapper<ProjectEntity>()
                .in("contract_id", idList));
        if (projectEntityList != null && projectEntityList.size() > 0) {
            //逻辑删除合同关联项目
            boolean b2 = projectService.update(new UpdateWrapper<ProjectEntity>()
                    .set("del_flag", DeleteFlag.YES.ordinal())
                    .in("contract_id", idList));
        }

        return b;
    }

    /**
     * 合同评审
     *
     * @param id
     */
    @Override
    public R contractExamine(Long id) {
        ContractEntity byId = this.getById(id);
        List<ProjectVo> projectVos = baseMapper.proJectVoList(id);

        R r = null;
        try {
            //调用用户是否存在接口
            String email = ShiroUtils.getEmail();
            String oaUserId = "";
            Map<String, Object> map = new HashMap<>();
            map.put("email", email);
            map.put("password", interface_password);
            cn.hutool.json.JSONObject jsonObject = JSONUtil.parseObj(map);
            String tokens = "";
            try {
                String userResult = HttpUtil.post(user_url, jsonObject.toString(), 10000);
                r = JSON.parseObject(userResult, R.class);
                Object o = r.get("code");

                if (o != null && "200".equals(o.toString())) {
                    String data = r.get("data").toString();
                    JSONObject token = JSONObject.parseObject(data);
                    tokens = token.get("token").toString();
                } else {
                    return r;
                }

            } catch (Exception e) {
                e.printStackTrace();
                log.error("调用用户是否存在接口" + e);
                if (e.toString().contains("Read timed out")) {
                    return R.error("调用云管家审用户是否存接口超时");
                } else {
                    return R.error("调用云管家审用户是否存在接口异常,请联系管理员");
                }
            }

            //调用云管家审批接口
            HashMap<String, Object> map1 = new HashMap<>();
            map1.put("bizContractInfo", byId);
            map1.put("bizContractProjectInfoList", projectVos);
            String post = HttpRequest.post(contract_approval)
//                    .header("cn_username", URLEncoder.encode(userName, "UTF-8"))
                    .header("token", tokens)
                    .timeout(10000)
                    .body(JSON.toJSONString(map1)).execute().body();

            R r2 = JSON.parseObject(post, R.class);
            Object o = r2.get("code");
            //调用接口成功
            if (o != null && "200".equals(o.toString())) {
                //修改合同审批状态
                ContractEntity contractEntity = new ContractEntity();
                contractEntity.setId(byId.getId());
                contractEntity.setReviewStatus(1);
                this.updateById(contractEntity);
                return R.ok("评审成功");
            } else {
                log.error(post);
                return r2;
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.error("调用云管家审批接口" + e);
            String s = e.toString();
            if (s.contains("Read timed out")) {
                return R.error("调用云管家审批接口超时");
            } else {
                return R.error("未知异常,请联系管理员");
            }

        }


    }

    /**
     * oa调用修改合同评审状态
     */
    @Override
    public R updateReviewStatus(ContractOaVo contractOaVo) {
        if (contractOaVo == null || contractOaVo.getReviewStatus() == null || contractOaVo.getId() == null) {

            return R.error("合同数据不能为空");
        }
        ContractEntity byId = this.getOne(new LambdaQueryWrapper<ContractEntity>().eq(ContractEntity::getId, contractOaVo.getId()).select(ContractEntity::getId, ContractEntity::getReviewStatus));
        if (byId == null) {
            return R.error("无此合同信息");
        }
        byId.setReviewStatus(contractOaVo.getReviewStatus());
        this.updateById(byId);

        return R.ok();
    }

    /**
     * 合同撤回审批
     */
    @Override
    public R retract(Long id) {
        ContractEntity one = this.getOne(new LambdaQueryWrapper<ContractEntity>().eq(ContractEntity::getId, id).select(ContractEntity::getIdentifier, ContractEntity::getId));
        HashMap<String, Object> map = new HashMap<>();
        map.put("contractId", id);
        String jsonString = JSON.toJSONString(map);


        //调云管家接口，撤回审批
        try {
            String post = HttpRequest.post(contract_cancellation)
                    .body(jsonString)
                    .timeout(10000)
                    .execute().body();

            R r = JSON.parseObject(post, R.class);
            Object o = r.get("code");
            //调用接口成功
            if (o != null && "200".equals(o.toString())) {
                //修改合同审批状态 //撤销状态
                one.setReviewStatus(4);

                this.updateById(one);
                return R.ok("撤销成功");
            } else {
                return r;
            }


        } catch (Exception e) {
            log.error("调用云管家撤回接口" + e);
            String s = e.toString();
            if (s.contains("Read timed out")) {
                return R.error("调用云管家撤回接口超时");
            } else {
                return R.error("未知异常,请联系管理员");
            }
        }

    }


}
