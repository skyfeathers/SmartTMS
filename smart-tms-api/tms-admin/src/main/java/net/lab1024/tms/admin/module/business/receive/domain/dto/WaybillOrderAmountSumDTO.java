package net.lab1024.tms.admin.module.business.receive.domain.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 根据运单统计已提交金额合计
 *
 * @author lidoudou
 * @date 2022/12/13 下午3:58
 */
@Data
public class WaybillOrderAmountSumDTO {

    /**
     * 运单ID
     */
    private Long waybillId;

    /**
     * 金额合计
     */
    private BigDecimal totalAmount;
}
