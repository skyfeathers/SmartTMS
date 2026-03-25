package net.lab1024.tms.admin.module.business.order.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.module.business.order.constant.GoodsUnitTypeEnum;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 订单商品
 *
 * @author lidoudou
 * @date 2022/12/6 下午5:25
 */
@Data
public class OrderGoodsImportForm {

    @ApiModelProperty("货物类型")
    private String goodsType;

    @ApiModelProperty("货物类型名称")
    private String goodsTypeName;

    @ApiModelProperty("货物名称")
    private String goodsName;

    @ApiModelProperty("货物量")
    private BigDecimal goodsQuantity;

    @ApiModelPropertyEnum(value = GoodsUnitTypeEnum.class, desc = "单位")
    private Integer goodsUnit;

}
