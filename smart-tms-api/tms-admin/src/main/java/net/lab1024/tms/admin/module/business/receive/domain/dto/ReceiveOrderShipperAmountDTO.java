package net.lab1024.tms.admin.module.business.receive.domain.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author lidoudou
 * @description:
 * @date 2023/6/14 上午11:15
 */
@Data
public class ReceiveOrderShipperAmountDTO {

    private Long shipperId;

    /**
     * 开票金额
     */
    private BigDecimal amount;
}