package net.lab1024.tms.fixedasset.module.business.consumables.stock.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 库存统计
 *
 * @author lidoudou
 * @date 2023/4/17 上午11:07
 */
@Data
public class ConsumablesStockStatisticVO {

    @ApiModelProperty("易耗品ID")
    private Long consumablesId;

    @ApiModelProperty("库存总值")
    private BigDecimal totalAmount;

    @ApiModelProperty("库存数量")
    private Integer stockCount;

    @ApiModelProperty("平均价")
    private BigDecimal averagePrice;
}
