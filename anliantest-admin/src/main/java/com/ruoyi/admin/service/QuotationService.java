package com.ruoyi.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.admin.domain.vo.QuotationListVo;
import com.ruoyi.admin.domain.vo.QuotationVo;
import com.ruoyi.admin.entity.QuotationEntity;
import com.ruoyi.admin.entity.QuotationInfoEntity;
import com.ruoyi.common.utils.R;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

/**
 * @author ZhuYiCheng
 * @date 2023/4/20 10:32
 */
public interface QuotationService extends IService<QuotationEntity> {

    /**
     * 新增报价单
     *
     * @param quotationVo
     * @return
     */
    Map<String, Object> addQuotation(QuotationVo quotationVo) throws UnsupportedEncodingException;


    /**
     * 报价单分页列表
     *
     * @param quotationListVo
     * @return
     */
    List<QuotationEntity> getQuotationList(QuotationListVo quotationListVo);

    /**
     * 新增合同,项目时获取客户公司状态为报价中的报价单
     * @param quotationEntity
     * @return
     */
    List<QuotationEntity> getQuotationListAll(QuotationEntity quotationEntity);

    /**
     * 报价单详情
     *
     * @param id
     * @return
     */
    QuotationVo getDetail(Long id);

    /**
     * 报价单修改
     *
     * @param quotationVo
     * @return
     */
    Map<String, Object> updateQuotation(QuotationVo quotationVo) throws UnsupportedEncodingException;

    /**
     * 终止报价单
     *
     * @param idList
     * @return
     */
    Boolean terminateQuotation(List<Long> idList);

    /**
     * 报价方案导出
     */
    void exportQuotation(HttpServletResponse response, Long id) throws IOException;

    /**
     * 最终报价方案导出
     */
    void exportFinalQuotation(HttpServletResponse response, Long id) throws IOException;

    /**
     * 修改报价单状态
     *
     * @return
     */
    Boolean updateStatus(QuotationEntity quotationEntity);

    /**
     * (逻辑)删除报价单
     * @return
     */
    Boolean deleteQuotation(List<Long> idList);

    /**
     * 报价单撤回审批
     * @param id
     * @return
     */
    R retract(Long id);

    /**
     * 导入报价详情(从文件解析)
     */
    QuotationVo importFromFile(MultipartFile file, HttpServletRequest request) throws Exception;

    /**
     * 根据报价单id获取报价单检测物质详情信息
     * @param id
     * @return
     */
    QuotationVo getInfoById(Long id);
}
