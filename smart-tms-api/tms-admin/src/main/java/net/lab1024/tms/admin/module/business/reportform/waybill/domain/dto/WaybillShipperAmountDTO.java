package net.lab1024.tms.admin.module.business.reportform.waybill.domain.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author lidoudou
 * @date 2023/3/4 下午3:09
 */
@Data
public class WaybillShipperAmountDTO {

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
    private BigDecimal receiveAmount;

    /**
     * 应付金额
     */
    private BigDecimal payAmount;

}
