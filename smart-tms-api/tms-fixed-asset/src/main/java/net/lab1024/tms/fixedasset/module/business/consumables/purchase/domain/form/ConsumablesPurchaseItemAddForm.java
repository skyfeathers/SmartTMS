package net.lab1024.tms.fixedasset.module.business.consumables.purchase.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 采购子表
 *
 * @author lidoudou
 * @date 2023/4/12 下午4:09
 */
@Data
public class ConsumablesPurchaseItemAddForm {

    @ApiModelProperty("资产ID")
    @NotNull(message = "资产ID不能为空")
    private Long consumablesId;

    @ApiModelProperty("采购价格")
    @NotNull(message = "采购价格不能为空")
    private BigDecimal price;

    @ApiModelProperty("采购数量")
    @NotNull(message = "采购数量不能为空")
    private Integer count;
}
