package net.lab1024.tms.admin.module.business.reportform.employee.domain.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 调度日报表
 *
 * @author zhaoxinyang
 * @date 2023/12/12 08:31
 */
@Data
public class ScheduleDayStatisticVO {

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

    @ApiModelProperty("运单量")
    @Excel(name = "运单量", width = 10)
    private Integer waybillCount;

    @ApiModelProperty("运单应付金额")
    private BigDecimal payableAmount;

    @ApiModelProperty("运费金额")
    @Excel(name = "派车金额", width = 10)
    private BigDecimal freightAmount;

    @ApiModelProperty("油卡金额")
    @Excel(name = "油卡金额", width = 10)
    private BigDecimal oilCardAmount;

    @ApiModelProperty("油卡占比")
    @Excel(name = "油卡占比", width = 10)
    private BigDecimal oilCardRate;

    @ApiModelProperty("未完成订单数量")
    @Excel(name = "异常数量", width = 10)
    private Integer inCompleteOrderCount;

    @ApiModelProperty("去年同期派车量")
    @Excel(name = "去年同期派车量", width = 15)
    private Integer lastYearWaybillCount;

    @ApiModelProperty("去年同期运单金额")
    @Excel(name = "去年同期运单金额", width = 20)
    private BigDecimal lastYearFreightAmount;

    @ApiModelProperty("比例")
    @Excel(name = "比例", width = 10)
    private BigDecimal rate;

}
