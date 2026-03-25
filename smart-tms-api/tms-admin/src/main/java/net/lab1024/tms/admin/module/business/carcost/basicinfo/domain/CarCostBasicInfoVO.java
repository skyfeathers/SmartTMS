package net.lab1024.tms.admin.module.business.carcost.basicinfo.domain;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 自有车-基本信息 VO
 *
 * @author zhaoxinyang
 * @date 2023/10/23 16:04
 */
@Data
public class CarCostBasicInfoVO {

    @ApiModelProperty("自有车基本信息ID")
    private Long basicInfoId;

    @ApiModelProperty("运单ID")
    private Long waybillId;

    @ApiModelProperty("运单号")
    private String waybillNumber;

    @ApiModelProperty("箱号")
    private String containerNumber;

    @ApiModelProperty("司机ID")
    private Long driverId;

    @ApiModelProperty("司机名字")
    private String driverName;

    @ApiModelProperty("车辆ID")
    private Long vehicleId;

    @ApiModelProperty("车牌号")
    private String vehicleNumber;

    @ApiModelProperty("高速里程")
    @Excel(name = "计入工资里程(高速) ", width = 20, orderNum = "8")
    private BigDecimal highSpeedMileage;

    @ApiModelProperty("低速里程")
    @Excel(name = "计入工资里程(低速) ", width = 20, orderNum = "9")
    private BigDecimal lowSpeedMileage;

    @ApiModelProperty("GPS里程")
    @Excel(name = "GPS公里数（KM） ", width = 20, orderNum = "10")
    private BigDecimal gpsMileage;

    @ApiModelProperty("用油量-升数")
    private BigDecimal oilConsumption;

    @ApiModelProperty("工资板块-基本工资")
    @Excel(name = "基本工资", width = 20, orderNum = "11")
    private BigDecimal basicSalary;

    @ApiModelProperty("工资板块-补助")
    @Excel(name = "补助", width = 20, orderNum = "12")
    private BigDecimal allowance;

    @ApiModelProperty("工资板块-提成工资")
    @Excel(name = "提成工资", width = 20, orderNum = "13")
    private BigDecimal commissionSalary;

    @ApiModelProperty("工资板块-出勤天数")
    @Excel(name = "出勤天数", width = 20, orderNum = "14")
    private Integer attendanceDays;

    @ApiModelProperty("小计")
    private BigDecimal salaryTotal;

    @ApiModelProperty("确认状态")
    private Boolean confirmFlag;

}