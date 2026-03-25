package net.lab1024.tms.admin.module.business.reportform.waybill.domain.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author lidoudou
 * @description:
 * @date 2023/5/11 上午11:05
 */
@Data
public class WaybillReceiveCostAmountDTO {

    /**
     * 货主ID
     */
    private Long shipperId;

    /**
     * 所属公司ID
     */
    private Long enterpriseId;

    /**
     * 派车方式
     */
    private Integer settleMode;

    /**
     * 应收金额
     */
    private BigDecimal totalAmount;

}