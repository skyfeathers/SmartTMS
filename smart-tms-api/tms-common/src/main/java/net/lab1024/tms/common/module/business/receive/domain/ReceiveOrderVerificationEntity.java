package net.lab1024.tms.common.module.business.receive.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 应收款 - 核销记录表
 *
 * @author lidoudou
 * @date 2022/7/22 上午9:36
 */
@Data
@TableName("t_receive_order_verification")
public class ReceiveOrderVerificationEntity {

    @TableId(type = IdType.AUTO)
    private Long recordId;

    /**
     * 应收款ID
     */
    private Long receiveOrderId;

    /**
     * 收款企业ID
     */
    private Long enterpriseId;

    /**
     * 开户银行
     */
    private String bankName;

    /**
     * 账户名称
     */
    private String accountName;

    /**
     * 账号
     */
    private String accountNumber;

    /**
     * 核销日期
     */
    private LocalDate verificationTime;

    /**
     * 核销金额
     */
    private BigDecimal verificationAmount;

    /**
     * 附件
     */
    private String attachment;

    /**
     * 流水单号
     */
    private String sequenceCode;

    /**
     * 备注
     */
    private String remark;

    private Long createUserId;

    private String createUserName;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
