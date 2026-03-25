package net.lab1024.tms.fixedasset.module.business.consumables.purchase.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 易耗品采购
 *
 * @author lidoudou
 * @date 2023/4/12 下午5:45
 */
@Data
public class ConsumablesPurchaseDetailVO extends ConsumablesPurchaseVO {

    @ApiModelProperty("采购详情")
    private List<ConsumablesPurchaseItemVO> itemList;

}
