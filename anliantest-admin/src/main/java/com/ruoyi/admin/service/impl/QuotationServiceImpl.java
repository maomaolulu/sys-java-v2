package com.ruoyi.admin.service.impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.data.RowRenderData;
import com.deepoove.poi.data.Rows;
import com.ruoyi.admin.domain.dto.ProjectWorkshopFromDto;
import com.ruoyi.admin.domain.poi.QuotationFinalTablePolicy;
import com.ruoyi.admin.domain.poi.QuotationTablePolicy;
import com.ruoyi.admin.domain.vo.QuotationListVo;
import com.ruoyi.admin.domain.vo.QuotationVo;
import com.ruoyi.admin.domain.vo.SubstanceVo;
import com.ruoyi.admin.entity.QuotationEntity;
import com.ruoyi.admin.entity.QuotationInfoEntity;
import com.ruoyi.admin.entity.WorkshopEntity;
import com.ruoyi.admin.mapper.QuotationInfoMapper;
import com.ruoyi.admin.mapper.QuotationMapper;
import com.ruoyi.admin.mapper.SubstanceMapper;
import com.ruoyi.admin.mapper.WorkshopMapper;
import com.ruoyi.admin.service.ProjectWorkshopService;
import com.ruoyi.admin.service.QuotationInfoService;
import com.ruoyi.admin.service.QuotationService;
import com.ruoyi.admin.service.WorkshopService;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.enums.DeleteFlag;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.R;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.pageUtil;
import com.ruoyi.system.common.CodeGenerateService;
import com.ruoyi.system.login.utils.ShiroUtils;
import com.spire.pdf.PdfDocument;
import com.spire.pdf.utilities.PdfTable;
import com.spire.pdf.utilities.PdfTableExtractor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author ZhuYiCheng
 * @date 2023/4/20 10:32
 */
@SuppressWarnings("unchecked")
@Service
@Slf4j
public class QuotationServiceImpl extends ServiceImpl<QuotationMapper, QuotationEntity> implements QuotationService {

    @Autowired
    private QuotationMapper quotationMapper;

    @Autowired
    private QuotationInfoMapper quotationInfoMapper;

    @Autowired
    private QuotationInfoService quotationInfoService;

    @Autowired
    private WorkshopMapper workshopMapper;

    @Autowired
    private WorkshopService workshopService;

    @Autowired
    private SubstanceMapper substanceMapper;

    @Autowired
    private CodeGenerateService codeGenerateService;

    @Value("${export.template.quotation_export}")
    private String quotation_export_path;
    @Value("${export.template.quotation_final_export}")
    private String quotation_final_export_path;
    //oa接口 人员
    @Value("${interface_out.oa.user_url}")
    private String user_url;
    //oa接口 报价审批
    @Value("${interface_out.oa.quotation_approval}")
    private String quotation_approval;
    //oa接口 报价撤销
    @Value("${interface_out.oa.quotation_cancellation}")
    private String quotation_cancellation;
    //MD5密码
    @Value("${interface_out.oa.interface_password}")
    private String interface_password;

    /**
     * 新增报价单
     *
     * @param quotationVo
     * @return
     */
    @Override
    @Transactional
    public Map<String, Object> addQuotation(QuotationVo quotationVo) throws UnsupportedEncodingException {
        Map<String, Object> map = new HashMap<>();
        //5. 检测项目分包比例大于20%不得创建报价单
        List<QuotationInfoEntity> quotationInfoEntityList = quotationVo.getQuotationInfoEntityList();
        //委托的
        List<QuotationInfoEntity> quotationInfoEntityList1 = quotationInfoEntityList.stream()
                .filter(q -> q.getIsEntrust() == 0).collect(Collectors.toList());
        //委托之后分包的
        List<QuotationInfoEntity> quotationInfoEntityList2 = quotationInfoEntityList.stream()
                .filter(q -> q.getIsSubcontract() == 0).collect(Collectors.toList());
        BigDecimal q1 = new BigDecimal(quotationInfoEntityList1.size());
        BigDecimal q2 = new BigDecimal(quotationInfoEntityList2.size());
        if (CollectionUtils.isNotEmpty(quotationInfoEntityList1)) {
            BigDecimal subcontractingRatio = q2.divide(q1, 3, BigDecimal.ROUND_HALF_UP);
            if (new BigDecimal("0.2").compareTo(subcontractingRatio) < 0) {
                //新定义状态码HttpStatus.UNSUPPORTED_DATA=420
//            throw new ServiceException("检测项目分包比例大于20%不得创建报价单", HttpStatus.UNSUPPORTED_DATA);
                map.put("code", 1);
                map.put("msg", "检测项目分包比例大于20%不得创建报价单");
                return map;
            }
        }
        QuotationEntity quotationEntity = quotationVo.getQuotationEntity();
        //重新计算检测费、总价、净值
        //分包费
        BigDecimal subprojectFee = quotationEntity.getSubprojectFee();
//        for (QuotationInfoEntity quotationInfoEntity : quotationInfoEntityList2) {
//            subprojectFee = subprojectFee.add(quotationInfoEntity.getSubtotal());
//        }

        //检测费
        BigDecimal detectFee = new BigDecimal(0);
        for (QuotationInfoEntity quotationInfoEntity : quotationInfoEntityList1) {
            if (quotationInfoEntity.getIsEntrust() == 0) {
                //实测点数
                BigDecimal point = new BigDecimal(quotationInfoEntity.getPoint());
                //重新计算 小计  实测点数×单价
                BigDecimal subtotal = point.multiply(quotationInfoEntity.getUnitPrice());
                quotationInfoEntity.setSubtotal(subtotal);
                detectFee = detectFee.add(subtotal);
            }
        }

        //总价=检测费+报告编制费+人工出车费
        BigDecimal totalMoney = detectFee.add(quotationEntity.getReportFee()).add(quotationEntity.getTrafficFee());
        //净值=优惠价-业务费-分包费-评审费-服务费-其他（职业病危害因素检测无需评审，则不需要评审费）
        BigDecimal netvalue = quotationEntity.getPreferentialFee()
                .subtract(quotationEntity.getCommission())
                .subtract(subprojectFee == null ? new BigDecimal(0) : subprojectFee)
                .subtract(quotationEntity.getEvaluationFee() == null ? new BigDecimal(0) : quotationEntity.getEvaluationFee())
                .subtract(quotationEntity.getServiceCharge() == null ? new BigDecimal(0) : quotationEntity.getServiceCharge())
                .subtract(quotationEntity.getOtherExpenses() == null ? new BigDecimal(0) : quotationEntity.getOtherExpenses());

        //重新设置分包费、检测费、总价、净值
        quotationEntity.setSubprojectFee(subprojectFee);
        quotationEntity.setDetectFee(detectFee);
        quotationEntity.setTotalMoney(totalMoney);
        quotationEntity.setNetvalue(netvalue);

        //优惠价
        BigDecimal preferentialFee = quotationEntity.getPreferentialFee();

        //折扣  优惠价/总价
        BigDecimal discount = preferentialFee.divide(totalMoney, 3, BigDecimal.ROUND_HALF_UP);
        // 将折扣放入报价基础系信息
        quotationEntity.setDiscount(discount);

        //isExamine为false，不走审批，进行各种条件判断,设置报价单状态为报价中（status= 3）
        if (quotationEntity.getIsExamine() != null && !quotationEntity.getIsExamine()) {

            //设置报价单状态为报价中（status= 3）
            quotationEntity.setStatus(3);

            //1. 创建的报价单优惠价不得低于2000元；
            BigDecimal b2000 = new BigDecimal(2000);
            int i = b2000.compareTo(preferentialFee);
            if (i > 0) {
                map.put("code", 2);
                map.put("msg", "创建的报价单优惠价不得低于2000元");
                return map;
            }
            //2. 业务费不得高于优惠价的20%
            //业务费
            BigDecimal commission = quotationEntity.getCommission();
            int i1 = commission.compareTo(preferentialFee.multiply(new BigDecimal("0.2")));
            if (i1 > 0) {
                map.put("code", 3);
                map.put("msg", "业务费不得高于优惠价的20%");
                return map;
            }
            //3. 总价低于2000元的项目，报价不得低于2000；

            //总价在2000~5000元（不包括5000）的项目，允许的折扣为90%，但又不得低于2000元；
            BigDecimal b5000 = new BigDecimal(5000);
            if (b2000.compareTo(totalMoney) <= 0 && b5000.compareTo(totalMoney) > 0) {
                if (new BigDecimal("0.9").compareTo(discount) > 0) {
                    map.put("code", 4);
                    map.put("msg", "总价在2000~5000元（不包括5000）的项目，允许的折扣为90%，但又不得低于2000元");
                    return map;
                }
            }
            //总价在5000~10000元（不包括10000）的项目，允许的折扣为80%
            BigDecimal b10000 = new BigDecimal(10000);
            if (b5000.compareTo(totalMoney) <= 0 && b10000.compareTo(totalMoney) > 0) {
                if (new BigDecimal("0.8").compareTo(discount) > 0) {
                    map.put("code", 5);
                    map.put("msg", "总价在5000~10000元（不包括10000）的项目，允许的折扣为80%");
                    return map;
                }
            }
            //总价在10000~20000元（不包括20000）的项目，允许的折扣为70%
            BigDecimal b20000 = new BigDecimal(20000);
            if (b10000.compareTo(totalMoney) <= 0 && b20000.compareTo(totalMoney) > 0) {
                if (new BigDecimal("0.7").compareTo(discount) > 0) {
                    map.put("code", 6);
                    map.put("msg", "总价在10000~20000元（不包括20000）的项目，允许的折扣为70%");
                    return map;
                }
            }
            //总价在20000~50000元（不包括50000）的项目，允许的折扣为60%
            BigDecimal b50000 = new BigDecimal(50000);
            if (b20000.compareTo(totalMoney) <= 0 && b50000.compareTo(totalMoney) > 0) {
                if (new BigDecimal("0.6").compareTo(discount) > 0) {
                    map.put("code", 7);
                    map.put("msg", "总价在20000~50000元（不包括50000）的项目，允许的折扣为60%");
                    return map;
                }

            }
            //总价在大于等于50000的项目，允许的折扣为50%
            if (b50000.compareTo(totalMoney) <= 0) {
                if (new BigDecimal("0.5").compareTo(discount) > 0) {
                    map.put("code", 8);
                    map.put("msg", "总价在大于等于50000的项目，允许的折扣为50%");
                    return map;
                }
            }
            //4. 以上，不满足任一条件，则该报价需要审批【云管家】
        }

        //填充各种信息
        Date nowDate = DateUtils.getNowDate();
        Long userId = ShiroUtils.getUserId();
        String userName = ShiroUtils.getUserName();
        String mobile = ShiroUtils.getUserEntity().getMobile();
        quotationEntity.setSalesmenId(userId);
        quotationEntity.setSalesmen(userName);
        quotationEntity.setSalesmenContact(mobile);
        quotationEntity.setCreateById(userId);
        quotationEntity.setCreateBy(userName);
        quotationEntity.setCreateTime(nowDate);
        quotationEntity.setUpdateBy(userName);
        quotationEntity.setUpdateTime(nowDate);
        //生成报价单编号
        String quotationCode = codeGenerateService.getQuotationCode();
        if (StringUtils.isBlank(quotationCode)) {
            map.put("code", 9);
            map.put("msg", "报价单编号获取失败,请联系管理员");
            return map;
        }
        quotationEntity.setCode(quotationCode);

        //isExamine为true，走审批，不进行各种条件判断,设置报价单状态为审批中（status= 1）
        if (quotationEntity.getIsExamine() != null && quotationEntity.getIsExamine()) {

            //设置报价单状态为审批中（status= 1）
            quotationEntity.setStatus(1);

            // 调云管家接口，走审批流程
            String salesmen = quotationEntity.getSalesmen();
            String email = ShiroUtils.getUserEntity().getEmail();
            Map<String, Object> map2 = new HashMap<>();
            map2.put("email", email);
            map2.put("password", interface_password);
            //验证业务员在OA系统中是否存在
//            String userResult = HttpUtil.get(user_url, map2, 1000);
            cn.hutool.json.JSONObject jsonObject1 = JSONUtil.parseObj(map2);
            String userResult = HttpUtil.post(user_url, jsonObject1.toString());
            JSONObject jsonObject = JSONObject.parseObject(userResult);
            String data = jsonObject.get("data").toString();
            JSONObject token = JSONObject.parseObject(data);
            String code200 = "200";
            Object code1 = jsonObject.get("code").toString();
            if (!code200.equals(code1)) {
                map.put("code", 9);
                map.put("msg", "您的信息在OA系统不存在");
                return map;
            }
            //调用云管家审批接口
            HashMap<String, Object> map3 = new HashMap<>();
            map3.put("user", jsonObject.get("data"));
            map3.put("bizQuotationApply", quotationEntity);

            String token1 = token.get("token").toString();

            String post = HttpRequest.post(quotation_approval)
                    .header("token", token1)
                    .body(JSON.toJSONString(map3)).execute().body();
            //对云管家返回值做处理
            HashMap<String, String> map1 = JSON.parseObject(post, new TypeReference<HashMap<String, String>>() {
            });

//            Map<String, String> map1 = new HashMap<>();
//            map1.put("code","200");
//            map1.put("msg","申请审批成功");
            String code = map1.get("code");
            String msg = map1.get("msg");


            String errorCode = "500";
            if (errorCode.equals(code)) {
                map.put("code", 9);
                map.put("msg", msg);
                return map;
            }
        }

        //存报价单基础信息
        quotationMapper.insert(quotationEntity);
        quotationInfoEntityList.forEach(quotationInfoEntity -> {
            quotationInfoEntity.setQuotationId(quotationEntity.getId());
            quotationInfoEntity.setCreateBy(userName);
            quotationInfoEntity.setCreateById(userId);
            quotationInfoEntity.setCreateTime(nowDate);
            quotationInfoEntity.setUpdateBy(userName);
            quotationInfoEntity.setUpdateTime(nowDate);
        });

        //存报价单详细信息列表
        quotationInfoService.saveBatch(quotationInfoEntityList);

        //取报价单基本信息id，放入车间岗位里
        List<WorkshopEntity> workshopEntityList = quotationVo.getWorkshopEntityList();
        workshopEntityList.forEach(workshopEntity -> {
            workshopEntity.setQuotationId(quotationEntity.getId());
//            workshopEntity.setQuotationCode(quotationEntity.getCode());
            workshopEntity.setCreateBy(userName);
            workshopEntity.setCreateById(userId);
            workshopEntity.setCreateTime(nowDate);
            workshopEntity.setUpdateBy(userName);
            workshopEntity.setUpdateTime(nowDate);
        });
        //存报价单车间岗位
        workshopService.saveBatch(workshopEntityList);

        map.put("code", 10);
        map.put("msg", "创建成功");
        return map;
    }


    /**
     * 报价单分页列表
     *
     * @param quotationListVo
     * @return
     */
    @Override
    public List<QuotationEntity> getQuotationList(QuotationListVo quotationListVo) {
        //将前端传的状态列表字符串转为集合
        String status = quotationListVo.getStatus();
        List<String> statusList = new ArrayList<>();
        if (StringUtils.isNotBlank(status)) {
            statusList = Arrays.asList(status.split(","));
        }
        //将前端传的报价类型列表字符串转为集合
        String quotationType = quotationListVo.getQuotationType();
        List<String> quotationTypeList = new ArrayList<>();
        if (StringUtils.isNotBlank(quotationType)) {
            quotationTypeList = Arrays.asList(quotationType.split(","));
        }

        QueryWrapper<QuotationEntity> queryWrapper = new QueryWrapper<QuotationEntity>()
                .like(StringUtils.isNotBlank(quotationListVo.getCode()), "code", quotationListVo.getCode())
                .like(StringUtils.isNotBlank(quotationListVo.getCompany()), "company", quotationListVo.getCompany())
                .eq(quotationListVo.getCompanyId() != null, "company_id", quotationListVo.getCompanyId())
                .eq(quotationListVo.getSalesmenId() != null, "salesmen_id", quotationListVo.getSalesmenId())
                .in(StringUtils.isNotBlank(status), "status", statusList)
                .in(StringUtils.isNotBlank(quotationType), "quotation_type", quotationTypeList)
                .eq("del_flag", DeleteFlag.NO.ordinal())
                .orderByDesc("create_time");

        pageUtil.startPage();
        return quotationMapper.selectList(queryWrapper);
    }

    /**
     * 新增合同,项目时获取客户公司状态为报价中的报价单
     *
     * @param quotationEntity
     */
    @Override
    public List<QuotationEntity> getQuotationListAll(QuotationEntity quotationEntity) {
        Long companyId = quotationEntity.getCompanyId();
        String company = quotationEntity.getCompany();
        Integer status = quotationEntity.getStatus();

        return quotationMapper.selectList(new QueryWrapper<QuotationEntity>()
                .eq(companyId != null, "company_id", companyId)
                .eq(StringUtils.isNotBlank(company), "company", company)
                .eq(status != null, "status", status));
    }

    /**
     * 报价单详情
     *
     * @param id
     * @return
     */
    @Override
    public QuotationVo getDetail(Long id) {
        QuotationVo quotationVo = new QuotationVo();
        //获取报价单基本信息
        QuotationEntity quotationEntity = quotationMapper.selectById(id);
        //获取报价详情列表
        List<QuotationInfoEntity> quotationInfoEntityList = quotationInfoMapper.selectList(new QueryWrapper<QuotationInfoEntity>().eq("quotation_id", id));
        //获取车间岗位列表
        List<WorkshopEntity> workshopEntityList = workshopMapper.selectList(new QueryWrapper<WorkshopEntity>().eq("quotation_id", id));

        quotationVo.setWorkshopEntityList(workshopEntityList);
        quotationVo.setQuotationEntity(quotationEntity);
        quotationVo.setQuotationInfoEntityList(quotationInfoEntityList);
        return quotationVo;
    }

    /**
     * 报价单修改
     *
     * @param quotationVo
     * @return
     */
    @Override
    public Map<String, Object> updateQuotation(QuotationVo quotationVo) throws UnsupportedEncodingException {

        Map<String, Object> map = new HashMap<>();
        //5. 检测项目分包比例大于20%不得创建报价单
        List<QuotationInfoEntity> quotationInfoEntityList = quotationVo.getQuotationInfoEntityList();
        //委托的
        List<QuotationInfoEntity> quotationInfoEntityList1 = quotationInfoEntityList.stream()
                .filter(q -> q.getIsEntrust() == 0).collect(Collectors.toList());
        //委托之后分包的
        List<QuotationInfoEntity> quotationInfoEntityList2 = quotationInfoEntityList.stream()
                .filter(q -> q.getIsSubcontract() == 0).collect(Collectors.toList());
        BigDecimal q1 = new BigDecimal(quotationInfoEntityList1.size());
        BigDecimal q2 = new BigDecimal(quotationInfoEntityList2.size());
        if (CollectionUtils.isNotEmpty(quotationInfoEntityList1)) {
            BigDecimal subcontractingRatio = q2.divide(q1, 3, BigDecimal.ROUND_HALF_UP);
            if (new BigDecimal("0.2").compareTo(subcontractingRatio) < 0) {
                map.put("code", 1);
                map.put("msg", "检测项目分包比例大于20%不得创建报价单");
                return map;
            }
        }
        QuotationEntity quotationEntity = quotationVo.getQuotationEntity();
        //重新计算检测费、总价、净值
        //分包费
        BigDecimal subprojectFee = quotationEntity.getSubprojectFee();
//        for (QuotationInfoEntity quotationInfoEntity : quotationInfoEntityList2) {
//            subprojectFee = subprojectFee.add(quotationInfoEntity.getSubtotal());
//        }

        //检测费
        BigDecimal detectFee = new BigDecimal(0);
        for (QuotationInfoEntity quotationInfoEntity : quotationInfoEntityList1) {
            if (quotationInfoEntity.getIsEntrust() == 0) {
                //实测点数
                BigDecimal point = new BigDecimal(quotationInfoEntity.getPoint());
                //重新计算 小计  实测点数×单价
                BigDecimal subtotal = point.multiply(quotationInfoEntity.getUnitPrice());
                quotationInfoEntity.setSubtotal(subtotal);
                detectFee = detectFee.add(subtotal);
            }
        }

        //总价=检测费+报告编制费+人工出车费
        BigDecimal totalMoney = detectFee.add(quotationEntity.getReportFee()).add(quotationEntity.getTrafficFee());
        //净值=优惠价-业务费-分包费-评审费-服务费-其他（职业病危害因素检测无需评审，则不需要评审费）
        BigDecimal netvalue = quotationEntity.getPreferentialFee()
                .subtract(quotationEntity.getCommission())
                .subtract(subprojectFee == null ? new BigDecimal(0) : subprojectFee)
                .subtract(quotationEntity.getEvaluationFee() == null ? new BigDecimal(0) : quotationEntity.getEvaluationFee())
                .subtract(quotationEntity.getServiceCharge() == null ? new BigDecimal(0) : quotationEntity.getServiceCharge())
                .subtract(quotationEntity.getOtherExpenses() == null ? new BigDecimal(0) : quotationEntity.getOtherExpenses());

        //重新设置分包费、检测费、总价、净值
        quotationEntity.setSubprojectFee(subprojectFee);
        quotationEntity.setDetectFee(detectFee);
        quotationEntity.setTotalMoney(totalMoney);
        quotationEntity.setNetvalue(netvalue);


        //isExamine为false，不走审批，进行各种条件判断
        if (quotationEntity.getIsExamine() != null && !quotationEntity.getIsExamine()) {
            //优惠价
            BigDecimal preferentialFee = quotationEntity.getPreferentialFee();
            //1. 创建的报价单优惠价不得低于2000元；
            BigDecimal b2000 = new BigDecimal(2000);
            int i = b2000.compareTo(preferentialFee);
            if (i > 0) {
                map.put("code", 2);
                map.put("msg", "创建的报价单优惠价不得低于2000元");
                return map;
            }
            //2. 业务费不得高于优惠价的20%
            //业务费
            BigDecimal commission = quotationEntity.getCommission();
            int i1 = commission.compareTo(preferentialFee.multiply(new BigDecimal("0.2")));
            if (i1 > 0) {
                map.put("code", 3);
                map.put("msg", "业务费不得高于优惠价的20%");
                return map;
            }
            //3. 总价低于2000元的项目，报价不得低于2000；
            //折扣  优惠价/总价
            BigDecimal discount = preferentialFee.divide(totalMoney, 3, BigDecimal.ROUND_HALF_UP);
            //总价在2000~5000元（不包括5000）的项目，允许的折扣为90%，但又不得低于2000元；
            BigDecimal b5000 = new BigDecimal(5000);
            if (b2000.compareTo(totalMoney) <= 0 && b5000.compareTo(totalMoney) > 0) {
                if (new BigDecimal("0.9").compareTo(discount) > 0) {
                    map.put("code", 4);
                    map.put("msg", "总价在2000~5000元（不包括5000）的项目，允许的折扣为90%，但又不得低于2000元");
                    return map;
                }
            }
            //总价在5000~10000元（不包括10000）的项目，允许的折扣为80%
            BigDecimal b10000 = new BigDecimal(10000);
            if (b5000.compareTo(totalMoney) <= 0 && b10000.compareTo(totalMoney) > 0) {
                if (new BigDecimal("0.8").compareTo(discount) > 0) {
                    map.put("code", 5);
                    map.put("msg", "总价在5000~10000元（不包括10000）的项目，允许的折扣为80%");
                    return map;
                }
            }
            //总价在10000~20000元（不包括20000）的项目，允许的折扣为70%
            BigDecimal b20000 = new BigDecimal(20000);
            if (b10000.compareTo(totalMoney) <= 0 && b20000.compareTo(totalMoney) > 0) {
                if (new BigDecimal("0.7").compareTo(discount) > 0) {
                    map.put("code", 6);
                    map.put("msg", "总价在10000~20000元（不包括20000）的项目，允许的折扣为70%");
                    return map;
                }
            }
            //总价在20000~50000元（不包括50000）的项目，允许的折扣为60%
            BigDecimal b50000 = new BigDecimal(50000);
            if (b20000.compareTo(totalMoney) <= 0 && b50000.compareTo(totalMoney) > 0) {
                if (new BigDecimal("0.6").compareTo(discount) > 0) {
                    map.put("code", 7);
                    map.put("msg", "总价在20000~50000元（不包括50000）的项目，允许的折扣为60%");
                    return map;
                }

            }
            //总价在大于等于50000的项目，允许的折扣为50%
            if (b50000.compareTo(totalMoney) <= 0) {
                if (new BigDecimal("0.5").compareTo(discount) > 0) {
                    map.put("code", 8);
                    map.put("msg", "总价在大于等于50000的项目，允许的折扣为50%");
                    return map;
                }
            }
            //4. 以上，不满足任一条件，则该报价需要审批【云管家】
        }

        //isExamine为true，走审批，不进行各种条件判断,报价单状态改为审批中（status= 1）
        if (quotationEntity.getIsExamine() != null && quotationEntity.getIsExamine()) {
            //编辑完之后要走审批，不管之前是什么状态，报价单状态改为审批中（status= 1）
            quotationEntity.setStatus(1);
            // 调云管家接口，走审批流程
//            String salesmen = quotationEntity.getSalesmen();
            String email = ShiroUtils.getUserEntity().getEmail();
            Map<String, Object> map2 = new HashMap<>();
//            map2.put("username", salesmen);
            map2.put("email", email);
            map2.put("password", interface_password);
            //验证业务员在OA系统中是否存在
            cn.hutool.json.JSONObject jsonObject1 = JSONUtil.parseObj(map2);
            String userResult = HttpUtil.post(user_url, jsonObject1.toString());
//            String userResult = HttpUtil.get(user_url, map2, 1000);

            JSONObject jsonObject = JSONObject.parseObject(userResult);
            String data = jsonObject.get("data").toString();
            JSONObject token = JSONObject.parseObject(data);
            String code200 = "200";
            Object code1 = jsonObject.get("code").toString();
            if (!code200.equals(code1)) {
                map.put("code", 9);
                map.put("msg", "您的信息在OA系统不存在");
                return map;
            }
            //调用云管家审批接口
            HashMap<String, Object> map3 = new HashMap<>();
            map3.put("user", jsonObject.get("data"));
            map3.put("bizQuotationApply", quotationEntity);

            String token1 = token.get("token").toString();

            String post = HttpRequest.post(quotation_approval)
                    .header("token", token1)
                    .body(JSON.toJSONString(map3)).execute().body();
            //对云管家返回值做处理
            HashMap<String, String> map1 = JSON.parseObject(post, new TypeReference<HashMap<String, String>>() {
            });
            String code = map1.get("code");
            String msg = map1.get("msg");

            String errorCode = "500";
            if (errorCode.equals(code)) {
                map.put("code", 9);
                map.put("msg", msg);
                return map;
            }
        }

        //修改报价单基本信息
        this.updateById(quotationEntity);
        //修改报价详情列表
        //删除原来的
        boolean b1 = quotationInfoService.remove(new QueryWrapper<QuotationInfoEntity>().eq("quotation_id", quotationEntity.getId()));
        //插入新的
        boolean b = quotationInfoService.saveBatch(quotationVo.getQuotationInfoEntityList());
        //修改车间岗位信息
        //删除原来的
        boolean b2 = workshopService.remove(new QueryWrapper<WorkshopEntity>().eq("quotation_id", quotationEntity.getId()));
        //插入新的
        boolean b3 = workshopService.saveBatch(quotationVo.getWorkshopEntityList());
        map.put("code", 10);
        map.put("msg", "修改成功");
        return map;
    }

    /**
     * 终止报价单
     *
     * @param idList
     * @return
     */
    @Override
    public Boolean terminateQuotation(List<Long> idList) {
        //根据id拿到要终止的报价单列表
        List<QuotationEntity> quotationEntityList = this.listByIds(idList);
        //修改报价单状态为丢单状态(报价单状态 1，审核中,2， 审核失败3， 报价中 4， 赢单5，丢单)
        quotationEntityList.forEach(quotationEntity -> quotationEntity.setStatus(5));
        //修改回去
        return this.updateBatchById(quotationEntityList);
    }

    @Override
    public void exportQuotation(HttpServletResponse response, Long id) throws IOException {

        Resource resource = new ClassPathResource(quotation_export_path);
        InputStream inputStream = resource.getInputStream();

        //基本信息
        QuotationEntity quotationEntities = this.getById(id);

        //表格
        List<QuotationInfoEntity> quotationInfoEntities = quotationInfoService.list(
                new LambdaQueryWrapper<QuotationInfoEntity>()
                        .eq(QuotationInfoEntity::getQuotationId, id)
                        .orderByAsc(QuotationInfoEntity::getWorkshop, QuotationInfoEntity::getPost)
        );
        if (!quotationInfoEntities.isEmpty()) {
            ArrayList<RowRenderData> rowRenderDataList = new ArrayList<>();

            for (int i = 0; i < quotationInfoEntities.size(); i++) {
    
                QuotationInfoEntity quo = quotationInfoEntities.get(i);        
                
                String substance = quo.getSubstance();
                Integer isEntrust = quo.getIsEntrust();
                Integer isSubcontract = quo.getIsSubcontract();
                if (isEntrust == 1) {
                    substance = "*" + substance;
                }
                if (isSubcontract == 0) {
                    substance = "#" + substance;
                }
                RowRenderData rowRenderData = Rows.of(String.valueOf(i + 1), quo.getWorkshop(), quo.getPost(), substance, quo.getShouldPoint().toString(), quo.getPoint().toString(), quo.getUnitPrice().toString(), quo.getSubtotal().toString()).center().create();
                rowRenderDataList.add(rowRenderData);
            }
            quotationEntities.setQuotationTable(rowRenderDataList);
        }
        try {
            Configure config = Configure.builder().bind("quotationTable", new QuotationTablePolicy()).build();
            XWPFTemplate template = XWPFTemplate.compile(inputStream, config).render(quotationEntities);


            // 设置强制下载不打开
            response.setContentType("application/force-download");
            // 设置文件名
            response.addHeader("Content-Disposition", "attachment;fileName=" + "world.docx;");
            template.writeAndClose(response.getOutputStream());
        } catch (Exception e) {
            log.error("报价方案导出失败", e);
        }
    }

    /**
     * 最终版报价导出
     * @param response
     * @param id
     * @throws IOException
     */
    @Override
    public void exportFinalQuotation(HttpServletResponse response, Long id) throws IOException {

        Resource resource = new ClassPathResource(quotation_final_export_path);
        InputStream inputStream = resource.getInputStream();

        //基本信息
        QuotationEntity quotationEntities = this.getById(id);

        //表格
        List<ProjectWorkshopFromDto> projectWorkshopFromDtos = baseMapper.workshopList(quotationEntities.getProjectId());

        if (!projectWorkshopFromDtos.isEmpty()) {
            ArrayList<RowRenderData> rowRenderDataList = new ArrayList<>();

            for (int i = 0; i < projectWorkshopFromDtos.size(); i++) {

                ProjectWorkshopFromDto quo = projectWorkshopFromDtos.get(i);


                RowRenderData rowRenderData = Rows.of(String.valueOf(i + 1),quo.getBuilding(), quo.getWorkshop(), quo.getPost(),  "", "", "", "","").center().create();
                rowRenderDataList.add(rowRenderData);
            }
            quotationEntities.setQuotationTable(rowRenderDataList);
        }
        try {
            Configure config = Configure.builder().bind("quotationTable", new QuotationFinalTablePolicy()).build();
            XWPFTemplate template = XWPFTemplate.compile(inputStream, config).render(quotationEntities);


            // 设置强制下载不打开
            response.setContentType("application/force-download");
            // 设置文件名
            response.addHeader("Content-Disposition", "attachment;fileName=" + "world.docx;");
            template.writeAndClose(response.getOutputStream());
        } catch (Exception e) {
            log.error("最终报价方案导出失败", e);
        }
    }


    /**
     * 修改报价单状态
     *
     * @return
     */
    @Override
    public Boolean updateStatus(QuotationEntity quotationEntity) {
        QuotationEntity quotationEntity1 = new QuotationEntity();
        quotationEntity1.setStatus(quotationEntity.getStatus());
        return this.update(quotationEntity1, new QueryWrapper<QuotationEntity>().eq("code", quotationEntity.getCode()));
    }

    /**
     * (逻辑)删除报价单
     *
     * @return
     */
    @Override
    public Boolean deleteQuotation(List<Long> idList) {
        List<QuotationEntity> quotationEntityList = new ArrayList<>();
        for (Long id : idList) {
            QuotationEntity quotationEntity = new QuotationEntity();
            quotationEntity.setId(id);
            quotationEntity.setDelFlag(DeleteFlag.YES.ordinal());
            quotationEntityList.add(quotationEntity);
        }
        boolean b = this.updateBatchById(quotationEntityList);
//        List<QuotationInfoEntity> infoEntityList = quotationInfoService.list(new QueryWrapper<QuotationInfoEntity>()
//                .in("quotation_id", idList)
//                .eq("del_flag", DeleteFlag.NO.ordinal()));
//        infoEntityList.forEach(i -> i.setDelFlag(DeleteFlag.YES.ordinal()));
//        boolean b1 = quotationInfoService.updateBatchById(infoEntityList);
//        return b && b1;
        return b;
    }

    /**
     * 报价单撤回审批
     *
     * @param id
     * @return
     */
    @Override
    public R retract(Long id) {
        QuotationEntity quotationEntity = this.getById(id);
        String code = quotationEntity.getCode();
        Map<String, String> map = new HashMap<>();
        map.put("code", code);
        String jsonString = JSON.toJSONString(map);
        //调云管家接口，撤回审批
        String post = HttpRequest.post(quotation_cancellation)
                .body(jsonString)
                .execute().body();
        //对云管家返回值做处理
        HashMap<String, String> map1 = JSON.parseObject(post, new TypeReference<HashMap<String, String>>() {
        });
//        Map<String, String> map1 = new HashMap<>();
//        map1.put("code","200");
//        map1.put("msg","申请审批成功");
        String code1 = map1.get("code");
        String msg = map1.get("msg");
        if (String.valueOf(HttpStatus.SUCCESS).equals(code1)) {
            //报价单状态改为撤销审核状态 status：6
            quotationEntity.setStatus(6);
            this.updateById(quotationEntity);
            return R.ok();
        }
        return R.error(msg);
    }

    @Override
    public QuotationVo importFromFile(MultipartFile file, HttpServletRequest request) throws Exception{
        String docLast=".doc",docxLast=".docx",pdfLast=".pdf";
        List<QuotationInfoEntity> quotationInfoEntities = new ArrayList<>();
        String fileName = file.getOriginalFilename();
        if (fileName.endsWith(docxLast) || fileName.endsWith(docLast)){
            InputStream inputStream = file.getInputStream();
            XWPFDocument doc = new XWPFDocument(inputStream);
            List<XWPFTable> tables = doc.getTables();
            XWPFTable xwpf = null;
            for (XWPFTable table:tables){
                List<XWPFTableRow> rowList = table.getRows();
                for (XWPFTableRow row : rowList){
                    List<XWPFTableCell> tableCells = row.getTableCells();
                    if ("序号".equals(tableCells.get(0).getText())){
                        // 找到要的表格
                        xwpf = table;
                    }
                }
            }
            List<XWPFTableRow> rows = xwpf.getRows();
            List<String> Tablerows = new ArrayList<>();
            for (XWPFTableRow row : rows) {
                List<XWPFTableCell> tableCells = row.getTableCells();
                if ("序号".equals(tableCells.get(0).getText())){
                    for (XWPFTableCell tableCell : tableCells) {
                        //添加表头列
                        Tablerows.add(tableCell.getText());
                    }
                }
            }
            for (XWPFTableRow row : rows) {
                QuotationInfoEntity entity = new QuotationInfoEntity();
                List<XWPFTableCell> tableCells = row.getTableCells();
                if (!"序号".equals(tableCells.get(0).getText()) && isInteger(tableCells.get(0).getText())){
                    for (int i = 0; i < tableCells.size(); i++) {
                        String title = Tablerows.get(i);
                        String nowString = tableCells.get(i).getText().replace(" ","");
                        switchSetText(title,nowString,entity);
                    }
                    quotationInfoEntities.add(entity);
                }
            }
        }else if (fileName.endsWith(pdfLast)){
            InputStream inputStream = file.getInputStream();
            PdfDocument pdf = new PdfDocument();
            pdf.loadFromStream(inputStream);

            //抽取表格
            PdfTableExtractor extractor = new PdfTableExtractor(pdf);
            PdfTable[] tableLists ;
            String lastWorkshop=null, lastPost=null;
            for (int page = 0; page < pdf.getPages().getCount(); page++) {
                tableLists = extractor.extractTable(page);
                if (tableLists != null && tableLists.length > 0 ) {
                    for (PdfTable table : tableLists)
                    {
                        int row = table.getRowCount();
                        int column = table.getColumnCount();
                        for (int i = 0; i < row; i++)
                        {
                            QuotationInfoEntity entity = new QuotationInfoEntity();
                            if (isInteger(table.getText(i,0))){
                                for (int j = 0; j < column; j++)
                                {
                                    String nowString = table.getText(i, j).replace(" ","");
                                    switch (j){
                                        case 1:
                                            if (StringUtils.isNotBlank(nowString)){
                                                entity.setWorkshop(nowString);
                                                lastWorkshop = nowString;
                                            }else {
                                                entity.setWorkshop(lastWorkshop);
                                            }
                                            break;
                                        case 2:
                                            if (StringUtils.isNotBlank(nowString)){
                                                entity.setPost(nowString);
                                                lastPost = nowString;
                                            }else {
                                                entity.setPost(lastPost);
                                            }
                                            break;
                                        case 3:
                                            if (nowString.contains("*")){
                                                entity.setIsEntrust(DeleteFlag.YES.ordinal());
                                                entity.setIsSubcontract(DeleteFlag.YES.ordinal());
                                                nowString = nowString.replace("*","");
                                            }else {
                                                entity.setIsEntrust(DeleteFlag.NO.ordinal());
                                            }
                                            nowString = nowString.replace("#","");
                                            entity.setSubstance(nowString);
                                            break;
                                        case 4:
                                            if (!isInteger(nowString)){ throw new Exception("第"+ (j+1) +"列下有除数字以外的字符"); }
                                            entity.setShouldPoint(Integer.parseInt(nowString));
                                            break;
                                        case 5:
                                            if (!isInteger(nowString)){ throw new Exception("第"+ (j+1) +"列下有除数字以外的字符"); }
                                            entity.setPoint(Integer.parseInt(nowString));
                                            break;
                                        case 6:
                                            entity.setUnitPrice(new BigDecimal(nowString));
                                            break;
                                        case 7:
                                            entity.setSubtotal(new BigDecimal(nowString));
                                            break;
                                        default:
                                            break;
                                    }
                                }
                                quotationInfoEntities.add(entity);
                            }
                        }
                    }
                }
            }
        }else {
            throw new Exception("导入的文件格式不正确,必须是pdf或者doc(docx)格式");
        }
        return updateQualification(quotationInfoEntities);
    }

    /**
     * 判断是否为整数
     * @param str 传入的字符串
     * @return 是整数返回true,否则返回false
     */
    private boolean isInteger(String str) {
        String NoBlankString = str.replace(" ","");
        String patternString = "[0-9]*";
        Pattern p = Pattern.compile(patternString);
        return p.matcher(NoBlankString).matches();
    }

    private void switchSetText(String title, String nowString, QuotationInfoEntity entity) throws Exception{
        switch (title){
            case "车间":
                entity.setWorkshop(nowString);
                break;
            case "岗位":
                entity.setPost(nowString);
                break;
            case "检测项目":
                if (nowString.contains("*")){
                    entity.setIsEntrust(DeleteFlag.YES.ordinal());
                    entity.setIsSubcontract(DeleteFlag.YES.ordinal());
                    nowString = nowString.replace("*","");
                }else {
                    entity.setIsEntrust(DeleteFlag.NO.ordinal());
                }
                nowString = nowString.replace("#","");
                entity.setSubstance(nowString);
                break;
            case "应测点数":
                if (!isInteger(nowString)){ throw new Exception(title + "下有除数字以外的字符"); }
                entity.setShouldPoint(Integer.parseInt(nowString));
                break;
            case "实测点数":
                if (!isInteger(nowString)){ throw new Exception(title + "下有除数字以外的字符"); }
                entity.setPoint(Integer.parseInt(nowString));
                break;
            case "单价（元）":
                entity.setUnitPrice(new BigDecimal(nowString));
                break;
            case "小计（元）":
                entity.setSubtotal(new BigDecimal(nowString));
                break;
            default:
                break;
        }
    }


    /**
     * 根据报价单id获取报价单检测物质详情信息
     *
     * @param id
     * @return
     */
    @Override
    public QuotationVo getInfoById(Long id) {
        List<QuotationInfoEntity> infoEntityList = quotationInfoService
                .list(new QueryWrapper<QuotationInfoEntity>().eq(id != null, "quotation_id", id));
        //根据实验室资质更新详情信息

        return updateQualification(infoEntityList);
    }



    /**
     * 根据物质库资质信息更新报价单检测项目详情信息
     *
     * @param infoEntityList
     * @return
     */
    private QuotationVo updateQualification(final List<QuotationInfoEntity> infoEntityList) {
        List<String> nameList = new ArrayList<>();
        for (QuotationInfoEntity quotationInfoEntity : infoEntityList) {
            nameList.add(quotationInfoEntity.getSubstance());
        }
        //根据检测项目名字查询物质库资质信息
        List<SubstanceVo> substanceVoList = substanceMapper.getById(new QueryWrapper<SubstanceVo>()
                .in(CollectionUtils.isNotEmpty(nameList), "s.name", nameList));
        for (SubstanceVo substanceVo : substanceVoList) {
            if (substanceVo.getQualification() == null) {
                substanceVo.setQualification(2);
                substanceVo.setLabSource("上海量远");
            }
        }

        Map<String, List<SubstanceVo>> map = substanceVoList.stream().collect(Collectors.groupingBy(SubstanceVo::getName));
        //搜集车间岗位信息
        List<WorkshopEntity> workshopEntityList = new ArrayList<>();
        for (QuotationInfoEntity quotationInfoEntity : infoEntityList) {
            WorkshopEntity workshopEntity = new WorkshopEntity();
            workshopEntity.setWorkshop(quotationInfoEntity.getWorkshop());
            workshopEntity.setPost(quotationInfoEntity.getPost());
            workshopEntityList.add(workshopEntity);
            //委托
            Integer isEntrust = quotationInfoEntity.getIsEntrust();
            //根据name取物质库信息
            List<SubstanceVo> voList = map.get(quotationInfoEntity.getSubstance());
            if (CollectionUtils.isNotEmpty(voList)) {
                //物质库里有该物质,默认获取列表的一个(噪声重复)
                SubstanceVo substanceVo = voList.get(0);
                //资质
                Integer qualification = substanceVo.getQualification();
                //不委托的，只更新资质信息，拿到的信息里都已设置不委托，不分包，小计为0，不再重新设置
                if (isEntrust == 0) {
                    //委托的，根据现有资质设置是否分包
                    if(qualification == 1){
                        //有资质,设置不分包
                        quotationInfoEntity.setIsSubcontract(1);
                    }else {
                        //无资质，设置分包
                        quotationInfoEntity.setIsSubcontract(0);
                    }
                }
                //重新设置资质
                quotationInfoEntity.setQualification(qualification);
                quotationInfoEntity.setSubstanceId(substanceVo.getId());
            } else {
                //物质库里无该物质,那就仅识别，不委托，不分包，无资质，单价和小计为0
                quotationInfoEntity.setIsEntrust(1);
                quotationInfoEntity.setIsSubcontract(1);
                quotationInfoEntity.setQualification(2);
                quotationInfoEntity.setSubstanceId(0L);
                quotationInfoEntity.setUnitPrice(new BigDecimal(0));
                quotationInfoEntity.setSubtotal(new BigDecimal(0));
            }
        }
        //车间岗位去重处理
        ArrayList<WorkshopEntity> newWorkshopList = workshopEntityList.stream()
                .collect(Collectors.collectingAndThen(Collectors
                        .toCollection(() -> new TreeSet<>(Comparator.comparing(o -> o.getWorkshop() + ";" + o.getPost()))), ArrayList::new));
        QuotationVo quotationVo = new QuotationVo();
        quotationVo.setQuotationInfoEntityList(infoEntityList);
        quotationVo.setWorkshopEntityList(newWorkshopList);
        return quotationVo;
    }

}
