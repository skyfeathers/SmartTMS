package net.lab1024.tms.common.module.business.contract.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 合同签署人信息
 *
 * @author lihaifan
 * @date 2022/9/25 17:54
 */
@Data
@TableName("t_contract_signer")
public class ContractSignerEntity {

    /**
     * 合同签署人ID
     */
    @TableId(type = IdType.AUTO)
    private Integer contractSignerId;

    /**
     * 合同ID
     */
    private Integer contractId;

    /**
     * 手机号/邮箱
     */
    private String account;

    /**
     * 姓名
     */
    private String name;

    /**
     * 证件号
     */
    private String certNo;

    /**
     * 个人证件类型（传certNo时certType必传）
     *
     * CRED_PSN_CH_IDCARD【大陆身份证】
     *
     * CRED_PSN_CH_TWCARD【台湾来往大陆通行证】
     *
     * CRED_PSN_CH_MACAO【澳门来往大陆通行证】
     *
     * CRED_PSN_CH_HONGKONG【香港来往大陆通行证】
     *
     * CRED_PSN_PASSPORT【护照】
     */
    private String certType;

    /**
     * 银行卡号
     */
    private String bankCardNo;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
