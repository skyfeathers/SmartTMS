package net.lab1024.tms.fixedasset.module.business.consumables.purchase.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 详情
 *
 * @author lidoudou
 * @date 2023/4/13 上午10:01
 */
@Data
public class ConsumablesPurchaseItemVO {

    private Long itemId;

    @ApiModelProperty("易耗品ID")
    private Long consumablesId;

    @ApiModelProperty("易耗材编码")
    private String consumablesNo;

    @ApiModelProperty("分类")
    private Long categoryId;

    @ApiModelProperty("分类名称")
    private String categoryName;

    @ApiModelProperty("易耗材名称")
    private String consumablesName;

    @ApiModelProperty("单价")
    private BigDecimal averagePrice;

    @ApiModelProperty("数量")
    private Integer stockCount;

    @ApiModelProperty("库存预警值")
    private Integer stockWarningValue;

    @ApiModelProperty("采购价格")
    private BigDecimal price;

    @ApiModelProperty("采购数量")
    private Integer count;
}
