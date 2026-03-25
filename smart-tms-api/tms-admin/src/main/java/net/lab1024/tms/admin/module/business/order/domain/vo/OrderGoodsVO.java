package net.lab1024.tms.admin.module.business.order.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.common.validator.enumeration.CheckEnum;
import net.lab1024.tms.common.module.business.order.constant.GoodsUnitTypeEnum;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author lidoudou
 * @description:
 * @date 2024/8/27 16:16
 */
@Data
public class OrderGoodsVO {

    @ApiModelProperty("订单货物ID")
    private Long orderGoodsId;

    @ApiModelProperty(value = "订单ID", hidden = true)
    private Long orderId;

    @ApiModelProperty("货物ID")
    private Long goodsId;

    @ApiModelProperty("货物类型")
    @NotNull(message = "货物类型不能为空")
    private String goodsType;

    @ApiModelProperty("货物类型名称")
    private String goodsTypeName;

    @ApiModelProperty("货物名称")
    @NotNull(message = "货物名称不能为空")
    private String goodsName;

    @ApiModelProperty("货物量")
    private BigDecimal goodsQuantity;

    @ApiModelPropertyEnum(value = GoodsUnitTypeEnum.class, desc = "单位")
    @CheckEnum(value = GoodsUnitTypeEnum.class,required = true,message = "单位错误")
    private Integer goodsUnit;

    @ApiModelProperty("剩余量")
    private BigDecimal remainGoodsQuantity;

    @ApiModelProperty("已用量")
    private BigDecimal waybillGoodsQuantity;

    @ApiModelProperty("备注")
    private String remark;

}