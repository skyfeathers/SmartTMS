package net.lab1024.tms.admin.module.business.reportform.receivereport.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 收入成本明细
 *
 * @author lidoudou
 * @date 2023/3/3 下午2:38
 */
@Data
public class EmployeeReceiveAmountVO {

    @ApiModelProperty("员工ID")
    private Long employeeId;

    @ApiModelProperty("员工姓名")
    private String employeeName;

    @ApiModelProperty("业务应收")
    private BigDecimal receiveAmount;

    @ApiModelProperty("业务应付")
    private BigDecimal payAmount;

    @ApiModelProperty("费用应付")
    private BigDecimal costAmount;

    @ApiModelProperty("税金")
    private BigDecimal taxAmount;

    @ApiModelProperty("毛利润")
    private BigDecimal profitAmount;

    @ApiModelProperty("毛利率")
    private BigDecimal profitRate;
}
