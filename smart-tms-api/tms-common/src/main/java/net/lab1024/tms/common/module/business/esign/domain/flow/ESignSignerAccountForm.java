package net.lab1024.tms.common.module.business.esign.domain.flow;

import lombok.Data;

/**
 * 签署方账号信息
 *
 * @author lihaifan
 * @date 2022/7/18 17:57
 */
@Data
public class ESignSignerAccountForm {

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
     * 通知方式，可选择多种通知方式，逗号分割，1-短信，2-邮件
     *
     * 注意：
     *
     * （1）可以通过此字段控制单个签署方的通知方式，不同的签署方可以设置不同的通知方式。
     *
     * https://open.esign.cn/doc/opendoc/saasapi-std/wwww1b_worixg
     *
     * （2）短信或邮件中的签署链接依然需要登录框。
     */
    private String noticeType;
}
