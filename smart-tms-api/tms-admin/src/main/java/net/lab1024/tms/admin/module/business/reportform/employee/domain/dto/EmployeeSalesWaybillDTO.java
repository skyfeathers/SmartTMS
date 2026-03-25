package net.lab1024.tms.admin.module.business.reportform.employee.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 运单所属销售DTO
 *
 * @author zhaoxinyang
 * @date 2023/12/10 11:52
 */
@Data
public class EmployeeSalesWaybillDTO {

    @ApiModelProperty("运单ID")
    private Long waybillId;

    @ApiModelProperty("应收金额")
    private BigDecimal receiveAmount;

    @ApiModelProperty("司机工资")
    private BigDecimal salaryAmount;

    @ApiModelProperty("在途费用")
    private BigDecimal carCostAmount;

    @ApiModelProperty("利润")
    private BigDecimal profitAmount;


    @ApiModelProperty("员工ID")
    private Long employeeId;

}
