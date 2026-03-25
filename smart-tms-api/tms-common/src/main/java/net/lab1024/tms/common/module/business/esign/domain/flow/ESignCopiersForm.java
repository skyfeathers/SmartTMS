package net.lab1024.tms.common.module.business.esign.domain.flow;

import lombok.Data;

/**
 * 抄送人列表
 *
 * @author lihaifan
 * @date 2022/7/18 18:09
 */
@Data
public class ESignCopiersForm {

    /**
     * 抄送主体类型, 0-个人, 1-企业
     */
    private Integer copierIdentityAccountType;

    /**
     * 抄送人的登录凭证,手机号或邮箱
     */
    private String copierAccount;

    /**
     * 抄送人account id
     */
    private String copierAccountId;

    /**
     * 抄送主体真实信息，个人手机号、邮箱或企业名称
     */
    private String copierIdentityAccount;

    /**
     * 抄送主体账号id
     */
    private String copierIdentityAccountId;
}
