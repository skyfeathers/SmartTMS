package net.lab1024.tms.common.module.business.receive.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.lab1024.tms.common.module.business.receive.constant.CheckStatusEnum;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 收款表
 *
 * @author lidoudou
 * @date 2022/7/20 下午5:07
 */
@Data
@TableName("t_receive_order")
public class ReceiveOrderEntity {

    @TableId(type = IdType.AUTO)
    private Long receiveOrderId;

    /**
     * 收款单号
     */
    private String receiveOrderNumber;

    /**
     * 订单所属公司
     */
    private Long enterpriseId;

    /**
     * 货主ID
     */
    private Long shipperId;

    /**
     * 是否需要开票
     */
    private Boolean makeInvoiceFlag;

    /**
     * 税点
     */
    private BigDecimal taxPoint;

    /**
     * 客服编辑的应收对账备注
     */
    private String remark;

    /**
     * 核算状态
     *
     * @see CheckStatusEnum
     */
    private Integer checkStatus;

    /**
     * 核算人
     */
    private Long checkUserId;

    /**
     * 核算人名称
     */
    private String checkUserName;

    /**
     * 核算附件
     */
    private String checkAttachment;

    /**
     * 核算备注
     */
    private String checkRemark;

    /**
     * 核算时间
     */
    private LocalDate checkTime;

    /**
     * 开票状态
     */
    private Integer invoiceStatus;

    /**
     * 核销状态
     */
    private Boolean verificationFlag;

    /**
     * 开票金额
     */
    private BigDecimal totalAmount;

    /**
     * 账期
     */
    private LocalDate accountPeriodDate;

    /**
     * 对账单附件
     */
    private String billAttachment;

    /**
     * 附件
     */
    private String attachment;

    /**
     * 业务日期，账单月份
     */
    private LocalDate businessDate;

    private Long createUserId;

    private String createUserName;

    private Long updateUserId;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
