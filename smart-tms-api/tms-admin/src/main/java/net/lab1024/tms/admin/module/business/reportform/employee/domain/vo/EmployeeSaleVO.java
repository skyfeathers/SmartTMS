package net.lab1024.tms.admin.module.business.reportform.employee.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 员工销售情况
 *
 * @author lidoudou
 * @date 2023/3/7 上午10:44
 */
@Data
public class EmployeeSaleVO {

    @ApiModelProperty("员工ID")
    private Long employeeId;

    @ApiModelProperty("员工姓名")
    private String employeeName;

//    @ApiModelProperty("企业ID")
//    private Long enterpriseId;
//
//    @ApiModelProperty("企业名称")
//    private String enterpriseName;

    @ApiModelProperty("目标")
    private BigDecimal targetAmount;

    @ApiModelProperty("完成金额")
    private BigDecimal completeAmount;

    @ApiModelProperty("完成率")
    private BigDecimal completeRate;

    @ApiModelProperty("去年同期目标")
    private BigDecimal lastYearTargetAmount;

    @ApiModelProperty("去年同期完成金额")
    private BigDecimal lastYearCompleteAmount;

    @ApiModelProperty("去年同期完成率")
    private BigDecimal lastYearCompleteRate;
}
