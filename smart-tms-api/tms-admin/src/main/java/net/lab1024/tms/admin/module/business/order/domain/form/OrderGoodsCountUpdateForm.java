package net.lab1024.tms.admin.module.business.order.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author lidoudou
 * @description:
 * @date 2023/5/25 下午2:25
 */
@Data
public class OrderGoodsCountUpdateForm {

    @ApiModelProperty("商品ID")
    @NotNull(message = "商品ID不能为空")
    private Long orderGoodsId;

    @ApiModelProperty("货物量")
    @NotNull(message = "货物量不能为空")
    private BigDecimal goodsQuantity;

}