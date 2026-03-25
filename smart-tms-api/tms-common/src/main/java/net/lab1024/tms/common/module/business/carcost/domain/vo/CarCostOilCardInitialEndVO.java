package net.lab1024.tms.common.module.business.carcost.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.module.business.oilcard.constant.OilCardFuelTypeEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CarCostOilCardInitialEndVO {

    @ApiModelProperty("油卡期初期末ID")
    private Long oilCardInitialEndId;

    @ApiModelProperty("运单ID")
    private Long waybillId;

    @ApiModelProperty("油卡ID")
    private Long oilCardId;

    @ApiModelProperty("油卡卡号")
    private String oilCardNo;

    @ApiModelPropertyEnum(desc = "燃油类型", value = OilCardFuelTypeEnum.class)
    private Integer fuelType;

    @ApiModelProperty("期初金额")
    private BigDecimal initialAmount;

    @ApiModelProperty("期末金额")
    private BigDecimal endAmount;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

}
