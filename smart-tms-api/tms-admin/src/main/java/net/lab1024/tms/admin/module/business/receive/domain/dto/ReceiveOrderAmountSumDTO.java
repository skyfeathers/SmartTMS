package net.lab1024.tms.admin.module.business.receive.domain.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 应收金额统计
 *
 * @author lidoudou
 * @date 2022/12/13 下午3:57
 */
@Data
public class ReceiveOrderAmountSumDTO {

    /**
     * 应收账款ID
     */
    private Long receiveOrderId;

    /**
     * 货主ID
     */
    private Long shipperId;

    /**
     * 金额合计
     */
    private BigDecimal totalAmount;
}
