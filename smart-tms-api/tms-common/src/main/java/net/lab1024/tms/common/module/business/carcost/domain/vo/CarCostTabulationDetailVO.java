package net.lab1024.tms.common.module.business.carcost.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.module.business.carcost.constant.CarCostCategoryPayModeEnum;
import net.lab1024.tms.common.module.business.oilcard.constant.OilCardFuelTypeEnum;

import java.time.LocalDateTime;

/**
 * 自有车列表 详情VO
 *
 * @author zhaoxinyang
 * @date 2023/11/02 09:57
 */
@Data
public class CarCostTabulationDetailVO extends CarCostTabulationVO {

    @ApiModelPropertyEnum(value = OilCardFuelTypeEnum.class, desc = "燃料类型")
    private Integer fuelType;

    @ApiModelPropertyEnum(value = CarCostCategoryPayModeEnum.class, desc = "支付方式")
    private Integer payMode;

    @ApiModelProperty("简称")
    private String shortName;

    @ApiModelProperty("订单编号")
    private String orderNo;

    @ApiModelProperty("路线")
    private String routeName;

    @ApiModelProperty("业务时间")
    private LocalDateTime businessDate;

    @ApiModelProperty("装/卸货时间")
    private LocalDateTime loadTime;

}
