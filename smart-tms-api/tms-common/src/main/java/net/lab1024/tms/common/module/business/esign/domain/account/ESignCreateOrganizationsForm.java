package net.lab1024.tms.common.module.business.esign.domain.account;

import lombok.Data;

/**
 * 创建机构签署账号
 *
 * @author lihaifan
 * @date 2022/7/14 15:44
 */
@Data
public class ESignCreateOrganizationsForm {

    /**
     * 创建机构账号的唯一标识。可将企业证件号、企业邮箱地址等作为此账号的唯一标识。
     *
     * 注：
     *
     * （1）创建机构账号和个人账号时，机构账号的唯一标识和个人账号的唯一标识不可重复；
     *
     * （2）开发者需保证字段thirdPartyUserId在平台方业务系统不可重复。
     */
    private String thirdPartyUserId;

    /**
     * 机构名称
     */
    private String name;

    /**
     * 证件类型，默认CRED_ORG_USCC
     *
     * （1）CRED_ORG_USCC 统一社会信用代码
     *
     * （2）CRED_ORG_REGCODE 工商注册号
     */
    private String idType;

    /**
     * 企业证件号，需传入真实存在的证件信息
     */
    private String idNumber;

    /**
     * 法定代表人证件号
     *
     * 注：法人证件号支持中国大陆身份证，台湾来往大陆通行证，港澳来往大陆通行证，护照这几种证件类型。但接口不做法人证件号格式校验。
     */
    private String orgLegalIdNumber;

    /**
     * 法定代表人名称
     */
    private String orgLegalName;
}
