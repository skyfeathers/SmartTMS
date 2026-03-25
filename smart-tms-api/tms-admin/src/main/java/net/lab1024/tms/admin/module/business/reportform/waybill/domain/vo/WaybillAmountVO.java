package net.lab1024.tms.admin.module.business.reportform.waybill.domain.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 员工应付金额
 *
 * @author lidoudou
 * @date 2023/3/9 上午10:59
 */
@Data
public class WaybillAmountVO {

    /**
     * 员工ID
     */
    private Long employeeId;

    /**
     * 运单ID
     */
    private Long waybillId;

    /**
     * 应付金额
     */
    private BigDecimal payableAmount;

}
