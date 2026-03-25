package net.lab1024.tms.common.module.business.esign.domain.account;

import lombok.Data;

/**
 * 创建个人签署账号
 *
 * @author lihaifan
 * @date 2022/7/14 15:44
 */
@Data
public class ESignCreateAccountsForm {

    /**
     * 创建个人账号的唯一标识。可将个人证件号、手机号、邮箱地址等作为此账号的唯一标识。
     *
     * 注：
     *
     * （1）创建个人账号和机构账号时，个人账号的唯一标识和机构账号的唯一标识不可重复；
     *
     * （2）开发者需保证字段thirdPartyUserId在平台方业务系统不可重复。
     */
    private String thirdPartyUserId;

    /**
     * 姓名
     */
    private String name;

    /**
     * 证件类型，默认CRED_PSN_CH_IDCARD
     *
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
     * 证件号，请传入真实的证件信息
     *
     * 注：身份证中有X字母的，需要传入大写的X
     */
    private String idNumber;

    /**
     * 手机号码，默认空。
     *
     * 注：手机号为空时无法接收签署短信通知并且无法使用短信验证方式进行意愿认证。
     */
    private String mobile;

    /**
     * 邮箱地址，默认空。
     *
     * 注：邮箱地址为空时无法接收签署邮件通知并且无法使用邮箱验证方式进行意愿认证。
     */
    private String email;
}
