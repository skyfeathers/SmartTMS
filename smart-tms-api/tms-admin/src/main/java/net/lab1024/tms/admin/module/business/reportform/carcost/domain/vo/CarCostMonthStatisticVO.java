package net.lab1024.tms.admin.module.business.reportform.carcost.domain.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 自有车月报表统计
 *
 * @author lidoudou
 * @date 2023/4/8 上午9:01
 */
@Data
public class CarCostMonthStatisticVO {

    @ApiModelProperty("车辆ID")
    private Long vehicleId;

    @ApiModelProperty("车牌号")
    @Excel(name = "车牌号")
    private String vehicleNumber;

    @ApiModelProperty("货重")
    @Excel(name = "货重")
    private BigDecimal goodsWeight;

    @ApiModelProperty("单趟应收运费")
    @Excel(name = "单趟应收运费（税后）", width = 20)
    private BigDecimal receiveFrightAmount;

    @ApiModelProperty("高速里程")
    @Excel(name = "计入工资里程（高速）", width = 24)
    private BigDecimal highSpeedMileage;

    @ApiModelProperty("低速里程")
    @Excel(name = "计入工资里程（低速）", width = 24)
    private BigDecimal lowSpeedMileage;

    @ApiModelProperty("GPS里程")
    @Excel(name = "GPS公里数（KM)", width = 20)
    private BigDecimal gpsMileage;

    @ApiModelProperty("用油量-升数")
    @Excel(name = "用量/升", width = 15)
    private BigDecimal oilConsumption;

    @ApiModelProperty("提成工资")
    @Excel(name = "提成工资", width = 15)
    private BigDecimal commissionSalary;

    @ApiModelProperty("维修费")
    @Excel(name = "维修费", width = 20)
    private BigDecimal repairAmount;

    @ApiModelProperty("现金支出")
    @Excel(name = "现金")
    private BigDecimal cashPayAmount;

    @ApiModelProperty("油费-专卡支出")
    @Excel(name = "油费-专卡")
    private BigDecimal dieselOilPayAmount;

    @ApiModelProperty("油费-普卡支出")
    @Excel(name = "油费-普卡")
    private BigDecimal gasolineOilPayAmount;

    @ApiModelProperty("etc支出")
    @Excel(name = "ETC")
    private BigDecimal etcPayAmount;

    @ApiModelProperty("尿素支出")
    @Excel(name = "尿素")
    private BigDecimal ureaPayAmount;

    @ApiModelProperty("保险费")
    @Excel(name = "保险费", width = 20)
    private BigDecimal insuranceAmount;

    @ApiModelProperty("审车费")
    @Excel(name = "审车费", width = 20)
    private BigDecimal reviewAmount;

    @ApiModelProperty("保养费")
    @Excel(name = "保养费", width = 20)
    private BigDecimal maintenanceAmount;

    @ApiModelProperty("支出")
    @Excel(name = "支出")
    private BigDecimal payAmount;

    /**
     * 收入? - 支出（incomeFlag = false）
     */
    @ApiModelProperty("利润 ")
    @Excel(name = "利润")
    private BigDecimal profitAmount;

    // 数值计算

    /**
     * 用油量 / 里程 * 100
     */
    @ApiModelProperty("油耗")
    @Excel(name = "油耗/升100km", width = 15)
    private BigDecimal oilConsumptionRate;

    @ApiModelProperty("成本占比")
    @Excel(name = "成本占比(%)", width = 15)
    private BigDecimal payRate;

    /**
     * 油卡充值费用  / 收入（运费）
     */
    @ApiModelProperty("油耗占比")
    @Excel(name = "油耗占比(%)", width = 15)
    private BigDecimal oilCardRate;

    /**
     * 尿素 / 收入
     */
    @ApiModelProperty("尿素占比")
    @Excel(name = "尿素占比(%)", width = 15)
    private BigDecimal ureaRate;

    /**
     * ETC过路费 / 收入
     */
    @ApiModelProperty("ETC占比")
    @Excel(name = "ETC占比(%)", width = 15)
    private BigDecimal roadRate;

    /**
     * 工资 / 收入
     */
    @ApiModelProperty("工资占比")
    @Excel(name = "工资占比(%)", width = 15)
    private BigDecimal salaryRate;

    /**
     * 现金  / 收入
     */
    @ApiModelProperty("其他占比")
    @Excel(name = "其他占比(%)", width = 15)
    private BigDecimal otherRate;

    /**
     * 利润  / 收入
     */
    @ApiModelProperty("利润占比")
    @Excel(name = "利润占比(%)", width = 15)
    private BigDecimal profitRate;

}
