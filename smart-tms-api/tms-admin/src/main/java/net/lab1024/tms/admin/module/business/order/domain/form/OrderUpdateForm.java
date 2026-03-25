package net.lab1024.tms.admin.module.business.order.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 更新订单
 *
 * @author lidoudou
 * @date 2022/7/13 下午3:20
 */
@Data
public class OrderUpdateForm extends OrderCreateForm {

    @ApiModelProperty("订单ID")
    @NotNull(message = "订单ID不能为空")
    private Long orderId;
}
