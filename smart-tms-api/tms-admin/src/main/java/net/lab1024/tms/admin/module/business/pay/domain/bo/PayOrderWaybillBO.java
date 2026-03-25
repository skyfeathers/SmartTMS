package net.lab1024.tms.admin.module.business.pay.domain.bo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author yandy
 * @description:
 * @date 2022/8/18 10:32 上午
 */
@Data
public class PayOrderWaybillBO {

    private Long payOrderId;

    private Long waybillId;

    /**
     * 应收总金额
     */
    private BigDecimal receiveAmount;

    /**
     * 利润金额
     */
    private BigDecimal profitAmount;

    private Long orderId;

    private String waybillNumber;

    private String containerNumber;
    /**
     * 回单附件
     */
    private String receiptAttachment;

    /**
     * 派车单附件
     */
    private String truckOrderAttachment;

    /**
     * 税金
     */
    private BigDecimal taxAmount;
}