package net.lab1024.tms.driver.module.business.waybill.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.module.business.order.constant.GoodsUnitTypeEnum;

import java.math.BigDecimal;

/**
 * @author zhuoda
 * @Date 2022-07-14
 */
@Data
public class WaybillGoodsVO {

    @ApiModelProperty("货物ID")
    private Long waybillGoodsId;

    private Long orderGoodsId;

    private Long waybillId;

    @ApiModelProperty("货物类型")
    private String goodsType;

    @ApiModelProperty("货物类型名称")
    private String goodsTypeName;

    @ApiModelProperty("货物名称")
    private String goodsName;

    @ApiModelPropertyEnum(value = GoodsUnitTypeEnum.class, desc = "单位")
    private Integer goodsUnit;

    @ApiModelProperty("货物量")
    private BigDecimal goodsQuantity;

    @ApiModelProperty("备注")
    private String remark;
}
