package net.lab1024.tms.fixedasset.module.business.consumables.stock.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 库存详情
 *
 * @author lidoudou
 * @date 2023/4/12 上午11:29
 */
@Data
public class ConsumablesDetailVO extends ConsumablesVO {

    @ApiModelProperty("库存列表")
    private List<ConsumablesStockVO> stockList;
}
