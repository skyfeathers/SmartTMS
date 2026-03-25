package net.lab1024.tms.admin.module.business.reportform.employee.domain.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class WaybillEmployeeAmountDTO {

    @ApiModelProperty("员工ID")
    private Long employeeId;

    @ApiModelProperty("应收金额")
    @Excel(name = "业务应收", orderNum = "2", width = 20)
    private BigDecimal receiveAmount;

    @ApiModelProperty("应付金额")
    @Excel(name = "业务应付", orderNum = "3", width = 20)
    private BigDecimal payableAmount;

    @ApiModelProperty("司机工资")
    private BigDecimal salaryAmount;

    @ApiModelProperty("在途费用")
    private BigDecimal carCostAmount;

    @ApiModelProperty("税金")
    @Excel(name = "税金", orderNum = "4", width = 20)
    private BigDecimal taxAmount;
}
