package net.lab1024.tms.common.module.business.carcost.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CarCostCashInitialEndVO {

    @ApiModelProperty("现金期初期末ID")
    private Long cashInitialEndId;

    @ApiModelProperty("运单ID")
    private Long waybillId;

    @ApiModelProperty("司机ID")
    private Long driverId;

    @ApiModelProperty("期初金额")
    private BigDecimal initialAmount;

    @ApiModelProperty("期末金额")
    private BigDecimal endAmount;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

}
