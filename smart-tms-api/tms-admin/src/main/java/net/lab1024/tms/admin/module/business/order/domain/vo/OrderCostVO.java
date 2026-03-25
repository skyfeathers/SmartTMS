package net.lab1024.tms.admin.module.business.order.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 费用明细详情
 *
 * @author lidoudou
 * @date 2022/8/1 下午8:23
 */
@Data
public class OrderCostVO {

    @ApiModelProperty("订单费用ID")
    private Long orderCostId;

    @ApiModelProperty("费用项类型")
    private Integer costItemType;

    @ApiModelProperty("费用项名称")
    private String costItemName;

    @ApiModelProperty("费用")
    private BigDecimal costAmount;

}
