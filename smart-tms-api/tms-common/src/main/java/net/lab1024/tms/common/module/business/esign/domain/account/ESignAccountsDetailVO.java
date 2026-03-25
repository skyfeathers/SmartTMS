package net.lab1024.tms.common.module.business.esign.domain.account;

import lombok.Data;

/**
 * 查询个人签署账号
 *
 * @author lihaifan
 * @date 2022/7/14 18:30
 */
@Data
public class ESignAccountsDetailVO {

    /**
     * 个人账号id
     */
    private String accountId;

    /**
     * 姓名
     */
    private String name;

    /**
     * 证件类型
     * （1）CRED_PSN_CH_IDCARD 大陆身份证，默认值
     *
     * （2）CRED_PSN_CH_TWCARD 台湾来往大陆通行证
     *
     * （3）CRED_PSN_CH_MACAO 澳门来往大陆通行证
     *
     * （4）CRED_PSN_CH_HONGKONG 香港来往大陆通行证
     *
     * （5）CRED_PSN_PASSPORT 护照
     */
    private String idType;

    /**
     * 证件号
     */
    private String idNumber;

    /**
     * 联系方式，手机号码
     */
    private String mobile;

    /**
     * 联系方式，邮箱地址
     */
    private String email;

    /**
     * 第三方平台的用户id
     */
    private String thirdPartyUserId;

    /**
     * 实名认证状态
     * true-已实名
     * false-未实名
     */
    private String status;

}
