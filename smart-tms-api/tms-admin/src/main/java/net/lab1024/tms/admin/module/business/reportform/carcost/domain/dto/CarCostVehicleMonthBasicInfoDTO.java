package net.lab1024.tms.admin.module.business.reportform.carcost.domain.dto;

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
public class CarCostVehicleMonthBasicInfoDTO {

    @ApiModelProperty("车辆ID")
    private Long vehicleId;

    @ApiModelProperty("高速里程")
    private BigDecimal highSpeedMileage;

    @ApiModelProperty("低速里程")
    private BigDecimal lowSpeedMileage;

    @ApiModelProperty("GPS里程")
    private BigDecimal gpsMileage;

    @ApiModelProperty("用油量-升数")
    private BigDecimal oilConsumption;

    @ApiModelProperty("工资板块-提成工资")
    private BigDecimal commissionSalary;

    @ApiModelProperty("工资板块-出勤天数")
    private Integer attendanceDays;

}