package net.lab1024.tms.admin.module.business.reportform.employee.domain.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 客服日报表
 *
 * @author zhaoxinyang
 * @date 2023/12/11 09:05
 */
@Data
public class CustomerServiceDayStatisticVO {

    @ApiModelProperty("员工ID")
    private Long employeeId;

    @ApiModelProperty("员工姓名")
    @Excel(name = "员工姓名", width = 10)
    private String employeeName;

    @ApiModelProperty("所属部门ID")
    private Long departmentId;

    @ApiModelProperty("所属部门")
    @Excel(name = "所属部门", width = 15)
    private String departmentName;

    @ApiModelProperty("业务所属公司")
    @Excel(name = "业务所属公司", width = 25)
    private String enterpriseName;

    @ApiModelProperty("订单量")
    @Excel(name = "订单量", width = 10)
    private Integer orderCount;

    @ApiModelProperty("运单量")
    @Excel(name = "运单量", width = 10)
    private Integer waybillCount;

    @ApiModelProperty("运单应收金额")
    @Excel(name = "运单应收金额", width = 15)
    private BigDecimal totalReceiveAmount;

    @ApiModelProperty("运单应付金额")
    @Excel(name = "运单应付金额", width = 15)
    private BigDecimal totalPayableAmount;

    @ApiModelProperty("异常订单量")
    @Excel(name = "异常订单量", width = 10)
    private Integer cancelOrderCount;

    @ApiModelProperty("提交应付单量")
    @Excel(name = "提交应付单量", width = 15)
    private Integer payOrderCount;

    @ApiModelProperty("应付金额")
    @Excel(name = "应付金额", width = 15)
    private BigDecimal payableAmount;

    @ApiModelProperty("去年同期运单量")
    @Excel(name = "去年同期运单量", width = 15)
    private Integer lastYearWaybillCount;

    @ApiModelProperty("去年同期运单金额")
    @Excel(name = "去年同期运单金额", width = 20)
    private BigDecimal lastYearWaybillAmount;

    @ApiModelProperty("比例")
    @Excel(name = "比例", width = 10)
    private BigDecimal rate;

}
