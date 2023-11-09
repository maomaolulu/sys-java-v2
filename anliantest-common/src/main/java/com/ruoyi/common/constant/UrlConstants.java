package com.ruoyi.common.constant;

/**
 * @author ZhuYiCheng
 * @date 2023/5/9 11:27
 */
public class UrlConstants {

    /**
     * 验证业务员在OA系统中是否存在
     */
    public static final String BJYZ="http://60.190.21.178:98/proxyJava/act/quotation_apply/info";
//    public static final String BJYZ="192.168.0.204:9527/act/quotation_apply/info";

    /**
     * 云管家报价单审批接口
     */
    public static final String BJSP="http://60.190.21.178:98/proxyJava/act/quotation_apply/save";
//    public static final String BJSP="192.168.0.204:9527/act/quotation_apply/save";

    /**
     * 云管家撤回报价单审批接口
     */
    public static final String CHSP="http://60.190.21.178:98/proxyJava/act/quotation_apply/revokeProcess";
//    public static final String CHSP="192.168.0.204:9527/act/quotation_apply/revokeProcess";
    /**
     * 云管家撤回合同审批接口
     */
//    public static final String CHHT="http://60.190.21.178:98/proxyJava/act/quotation_apply/revokeProcess";
    public static final String CHHT="192.168.0.204:9527/act/contract_review/revokeProcess";

    /**
     * 云管家合同审批接口
     */
    public static final String HTSP="192.168.0.204:9527/act/contract_review/save";
}
