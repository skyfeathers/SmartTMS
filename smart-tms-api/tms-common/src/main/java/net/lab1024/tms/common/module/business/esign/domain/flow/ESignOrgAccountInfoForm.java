package net.lab1024.tms.common.module.business.esign.domain.flow;

import lombok.Data;

/**
 * 机构签署主体基本信息
 *
 * 注：当签署方为机构的情形，可以传入机构名称对应的其他基本信息，会带入到签署页面中
 *
 * @author lihaifan
 * @date 2022/9/16 10:35
 */
@Data
public class ESignOrgAccountInfoForm {

    /**
     * 组织机构证件号
     */
    private String certNo;

    /**
     * 组织机构证件类型
     *
     * CRED_ORG_USCC【统一社会信用代码】
     *
     * CRED_ORG_REGCODE【工商注册号】
     */
    private String certType;

    /**
     * 法定代表人姓名
     */
    private String legalRepName;

    /**
     * 法定代表人证件号
     */
    private String legalRepCertNo;

    /**
     * 法定代表人证件类型，不传默认：中国大陆身份证
     *
     * CRED_PSN_CH_IDCARD【中国大陆身份证】CRED_PSN_CH_TWCARD【台湾来往大陆通行证】
     *
     * CRED_PSN_CH_MACAO【澳门来往大陆通行证】
     *
     * CRED_PSN_CH_HONGKONG【香港来往大陆通行证】
     *
     * CRED_PSN_PASSPORT【护照】
     */
    private String legalRepCertType;
}
