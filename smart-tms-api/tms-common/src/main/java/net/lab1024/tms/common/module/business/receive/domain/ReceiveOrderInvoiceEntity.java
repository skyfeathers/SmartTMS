package net.lab1024.tms.common.module.business.receive.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.lab1024.tms.common.module.business.receive.constant.InvoiceKindTypeEnum;
import net.lab1024.tms.common.module.business.receive.constant.InvoiceTypeEnum;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 应收帐款的开票信息
 *
 * @author lidoudou
 * @date 2022/8/15 下午5:07
 */
@Data
@TableName("t_receive_order_invoice")
public class ReceiveOrderInvoiceEntity {

    @TableId(type = IdType.AUTO)
    private Long receiveOrderInvoiceId;

    private Long receiveOrderId;

    /**
     * 发票类型
     *
     * @see InvoiceTypeEnum
     */
    private Integer invoiceType;

    /**
     * 发票种类
     *
     * @see InvoiceKindTypeEnum
     */
    private Integer invoiceKindType;

    /**
     * 开票金额
     */
    private BigDecimal invoiceAmount;

    /**
     * 纳税识别号
     */
    private String invoiceNo;

    /**
     * 开票抬头
     */
    private String invoiceName;

    /**
     * 开票电话
     */
    private String invoicePhone;

    /**
     * 开票银行
     */
    private String invoiceBankName;

    /**
     * 开户行号
     */
    private String invoiceBankNo;


    /**
     * 开票地址
     */
    private String invoiceAddress;

    /**
     * 快递单号
     */
    private String courierNumber;

    /**
     * 开票状态
     */
    private Integer invoiceStatus;

    /**
     * 开票人
     */
    private Long invoiceUserId;

    /**
     * 开票人姓名
     */
    private String invoiceUserName;

    /**
     * 发票号
     */
    private String invoiceNumber;

    /**
     * 开票备注
     */
    private String invoiceRemark;

    /**
     * 开票附件
     */
    private String invoiceAttachment;

    /**
     * 开票时间
     */
    private LocalDate invoiceTime;


    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
