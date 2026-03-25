package net.lab1024.tms.admin.module.business.reportform.employee.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 根据员工查询付款单
 *
 * @author zhaoxinyang
 * @date 2023/12/11
 */
@Data
public class EmployeePayOrderCostDTO {

    @ApiModelProperty("付款单ID")
    private Long payOrderId;

    @ApiModelProperty("应付金额")
    private BigDecimal payableTotalAmount;

    @ApiModelProperty("客服ID")
    private Long customerServiceId;

}
