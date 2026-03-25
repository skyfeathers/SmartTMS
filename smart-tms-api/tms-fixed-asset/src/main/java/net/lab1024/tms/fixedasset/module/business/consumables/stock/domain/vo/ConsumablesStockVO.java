package net.lab1024.tms.fixedasset.module.business.consumables.stock.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 库存列表展示
 *
 * @author lidoudou
 * @date 2023/4/12 上午11:29
 */
@Data
public class ConsumablesStockVO {

    @ApiModelProperty("所在位置 - ID")
    private Long locationId;

    @ApiModelProperty("所在位置名称")
    private String locationName;

    @ApiModelProperty("易耗品ID")
    private Long consumablesId;

    @ApiModelProperty("平均价")
    private BigDecimal averagePrice;

    @ApiModelProperty("库存数量")
    private Integer stockCount;

    @ApiModelProperty("总金额")
    private BigDecimal totalAmount;

    @ApiModelProperty("创建人")
    private String createUserName;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

}
