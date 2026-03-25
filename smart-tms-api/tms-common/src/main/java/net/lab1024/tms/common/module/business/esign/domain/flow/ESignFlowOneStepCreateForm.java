package net.lab1024.tms.common.module.business.esign.domain.flow;

import lombok.Data;

/**
 * 一步发起签章
 *
 * @author lihaifan
 * @date 2022/7/18 21:08
 */
@Data
public class ESignFlowOneStepCreateForm {

    /**
     * 企业ID
     */
    private Long enterpriseId;

    /**
     * 文件ID
     */
    private String fileId;

    /**
     * 流程名称
     */
    private String businessScene;

    /**
     * 平台签署关键字
     */
    private String platformKeyword;

    /**
     * 平台时间关键字
     */
    private String platformTimeKeyword;

    /**
     * 用户签署关键字
     */
    private String userKeyword;

    /**
     * 用户时间关键字
     */
    private String userTimeKeyword;

    /**
     * 签署人真实信息，手机号或邮箱
     *
     * 注：
     *
     * （1）传入手机号/邮箱（e签宝SaaS官网的登录手机号或邮箱）
     *
     * （2）当平台用户签署时该参数必传
     */
    private String signerAccount;

    /**
     * 签署人基本信息
     *
     * 注：所传入个人的基本信息，会代入到签署页面里，
     */
    private ESignSignerAccountInfoForm signerAccountInfo;

    /**
     * 签署主体真实信息，个人手机号、邮箱或机构名称
     *
     * 注：
     *
     * 1、机构签署场景此字段必须传机构名称，个人签署场景可以不传或者传个人的手机号、邮箱。
     *
     * 机构名词解释：企业、事业单位、机关、社会团体及其他依法成立的单位的统称。
     */
    private String authorizedAccount;

    /**
     * 机构签署主体基本信息
     *
     * 注：当签署方为机构的情形，可以传入机构名称对应的其他基本信息，会带入到签署页面中
     */
    private ESignOrgAccountInfoForm orgAccountInfo;

    /**
     * 第三方流水号
     */
    private String thirdOrderNo;
}
