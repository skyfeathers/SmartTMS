package net.lab1024.tms.admin.module.business.order.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author lidoudou
 * @description:
 * @date 2023/5/25 下午2:15
 */
@Data
public class OrderScheduleFlagUpdateForm {

    @ApiModelProperty("订单ID")
    @NotNull(message = "订单ID不能为空")
    private Long orderId;

    @ApiModelProperty("商品列表")
    @NotEmpty(message = "商品列表不能为空")
    @Valid
    private List<OrderGoodsCountUpdateForm> goodsList;
}