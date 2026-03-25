package net.lab1024.tms.common.module.business.esign.domain.account;

import lombok.Data;

/**
 * 查询机构签署账号
 *
 * @author lihaifan
 * @date 2022/7/14 18:30
 */
@Data
public class ESignOrganizationsDetailVO {

    /**
     * 机构账号Id
     */
    private String orgId;

    /**
     * 创建人账号id
     */
    private String creator;

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
     * 企业证件号
     */
    private String idNumber;

    /**
     * 第三方平台的机构id
     */
    private String thirdPartyUserId;

    /**
     * 法定代表人证件号
     */
    private String orgLegalIdNumber;

    /**
     * 法定代表人名称
     */
    private String orgLegalName;

    /**
     * 实名认证状态
     * true-已实名
     * false-未实名
     */
    private String status;
}
