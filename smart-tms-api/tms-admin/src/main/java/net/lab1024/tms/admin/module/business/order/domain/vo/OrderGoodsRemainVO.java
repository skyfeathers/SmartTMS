package net.lab1024.tms.admin.module.business.order.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 订单货物剩余对象
 * @author zhuoda
 * @date 2022-07-22 21:30:45
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderGoodsRemainVO {

    @ApiModelProperty("货物ID")
    private Long orderGoodsId;

    @ApiModelProperty("毛重")
    private BigDecimal goodsWeight;

    @ApiModelProperty("数量")
    private Integer goodsCount;
}
