package net.lab1024.tms.admin.module.business.reportform.employee.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.module.business.order.constant.OrderStatusEnum;

/**
 * 客服日报表统计订单
 *
 * @author zhaoxinyang
 * @date 2023/12/11 09:38
 */
@Data
public class EmployeeCustomerServiceOrderDTO {

    @ApiModelProperty("订单ID")
    private Long orderId;

    @ApiModelPropertyEnum(value = OrderStatusEnum.class, desc = "订单状态")
    private Integer orderStatus;

    @ApiModelProperty("客服ID")
    private Long customerServiceId;

}
