package net.lab1024.tms.admin.module.business.reportform.carcost.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 自有车日/月费用统计
 *
 * @author zhaoxinyang
 * @date 2024/10/9 9:16
 */
@Data
public class CarCostDayOrMonthStatisticVO {

    @ApiModelProperty("车辆ID")
    private Long vehicleId;

    @ApiModelProperty("车牌号")
    private String vehicleNumber;

    @ApiModelProperty("收入")
    private BigDecimal income;

    @ApiModelProperty("支出")
    private BigDecimal expenditure;

    @ApiModelProperty("毛利")
    private BigDecimal profit;

}
