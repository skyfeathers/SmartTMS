package net.lab1024.tms.common.module.business.contract.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 合同签署企业信息
 *
 * @author lihaifan
 * @date 2022/9/25 18:00
 */
@Data
@TableName("t_contract_org")
public class ContractOrgEntity {

    /**
     * 合同签署人ID
     */
    @TableId(type = IdType.AUTO)
    private Integer contractOrgId;

    /**
     * 合同ID
     */
    private Integer contractId;

    /**
     * 个人手机号、邮箱或机构名称
     */
    private String account;

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

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
