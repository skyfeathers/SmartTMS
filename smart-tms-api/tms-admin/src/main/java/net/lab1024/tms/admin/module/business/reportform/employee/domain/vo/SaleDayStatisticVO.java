package net.lab1024.tms.admin.module.business.reportform.employee.domain.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 销售日报表查询
 *
 * @author zhaoxinyang
 * @date 2023/12/10 09:27
 */
@Data
public class SaleDayStatisticVO {

    @ApiModelProperty("员工ID")
    private Long employeeId;

    @ApiModelProperty("员工姓名")
    @Excel(name = "员工姓名", width = 10)
    private String employeeName;

    @ApiModelProperty("所属部门ID")
    private Long departmentId;

    @ApiModelProperty("所属部门")
    @Excel(name = "所属部门", width = 10)
    private String departmentName;

    @ApiModelProperty("月指标")
    @Excel(name = "月指标", width = 10)
    private BigDecimal targetAmount;

    @ApiModelProperty("已完成指标")
    @Excel(name = "已完成指标", width = 10)
    private BigDecimal completedAmount;

    @ApiModelProperty("未完成指标")
    @Excel(name = "未完成指标", width = 10)
    private BigDecimal inCompleteAmount;

    @ApiModelProperty("完成比例")
    @Excel(name = "完成比例", width = 10)
    private BigDecimal completeRate;

    @ApiModelProperty("去年同期月指标")
    @Excel(name = "去年同期月指标", width = 15)
    private BigDecimal lastYearTargetAmount;

    @ApiModelProperty("去年同期已完成指标")
    private BigDecimal lastYearCompletedAmount;

    @ApiModelProperty("比例")
    @Excel(name = "比例", width = 10)
    private BigDecimal rate;

    @ApiModelProperty("运单量")
    @Excel(name = "运单量", width = 10)
    private Integer waybillCount;

    @ApiModelProperty("应收金额")
    private BigDecimal receiveAmount;

    @ApiModelProperty("运费金额")
    @Excel(name = "运费金额", width = 10)
    private BigDecimal freightAmount;

    @ApiModelProperty("油卡金额")
    @Excel(name = "油卡金额", width = 10)
    private BigDecimal oilCardAmount;

    @ApiModelProperty("异常金额")
    @Excel(name = "异常金额", width = 10)
    private BigDecimal exceptionAmount;

    @ApiModelProperty("司机工资")
    @Excel(name = "司机工资", width = 10)
    private BigDecimal salaryAmount;

    @ApiModelProperty("在途费用")
    @Excel(name = "在途费用", width = 10)
    private BigDecimal carCostAmount;

    @ApiModelProperty("税后毛利额")
    @Excel(name = "税后毛利额", width = 10)
    private BigDecimal profitAmount;

    @ApiModelProperty("毛利率")
    @Excel(name = "毛利率", width = 10)
    private BigDecimal profitRate;

}
