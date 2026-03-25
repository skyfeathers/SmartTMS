package net.lab1024.tms.admin.module.business.order.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.common.validator.enumeration.CheckEnum;
import net.lab1024.tms.common.module.business.order.constant.GoodsUnitTypeEnum;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 订单详情展示
 *
 * @author lidoudou
 * @date 2022/7/13 下午7:56
 */
@Data
public class OrderGoodsDTO {

    @ApiModelProperty("货物ID")
    private Long orderGoodsId;

    @ApiModelProperty(value = "订单ID", hidden = true)
    private Long orderId;

    @ApiModelProperty("货物类型")
    @NotNull(message = "货物类型不能为空")
    private String goodsType;

    @ApiModelProperty("货物类型名称")
    private String goodsTypeName;

    @ApiModelProperty("货物名称")
    @NotNull(message = "货物名称不能为空")
    private String goodsName;

    @ApiModelProperty("货物量")
    @NotNull(message = "货物量")
    private BigDecimal goodsQuantity;

    @ApiModelProperty("剩余货物量")
    private BigDecimal remainGoodsQuantity;

    @ApiModelProperty("运单已用货物量")
    private BigDecimal waybillGoodsQuantity;

    @ApiModelPropertyEnum(value = GoodsUnitTypeEnum.class, desc = "单位")
    @CheckEnum(value = GoodsUnitTypeEnum.class,required = true,message = "单位错误")
    private Integer goodsUnit;

    @ApiModelProperty("备注")
    private String remark;
}
