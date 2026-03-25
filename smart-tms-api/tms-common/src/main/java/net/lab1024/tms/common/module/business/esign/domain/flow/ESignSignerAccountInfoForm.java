package net.lab1024.tms.common.module.business.esign.domain.flow;

import lombok.Data;

/**
 * 签署方账号信息-签署人基本信息
 *
 * 注：所传入个人的基本信息，会代入到签署页面里，
 *
 * @author lihaifan
 * @date 2022/9/16 10:34
 */
@Data
public class ESignSignerAccountInfoForm {

    /**
     * 个人姓名
     */
    private String name;

    /**
     * 个人证件号（传certType时certNo必传）
     */
    private String certNo;

    /**
     * 个人证件类型（传certNo时certType必传）
     *
     * CRED_PSN_CH_IDCARD【大陆身份证】CRED_PSN_CH_TWCARD【台湾来往大陆通行证】
     *
     * CRED_PSN_CH_MACAO【澳门来往大陆通行证】
     *
     * CRED_PSN_CH_HONGKONG【香港来往大陆通行证】
     *
     * CRED_PSN_PASSPORT【护照】
     */
    private String certType;

    /**
     * 银行卡号（银行卡四要素认证时可传入）
     */
    private String bankCardNo;

}
