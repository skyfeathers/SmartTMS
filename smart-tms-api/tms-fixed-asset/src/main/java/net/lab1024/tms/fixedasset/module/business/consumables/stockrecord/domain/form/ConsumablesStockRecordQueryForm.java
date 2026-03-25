package net.lab1024.tms.fixedasset.module.business.consumables.stockrecord.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.domain.PageParam;

import javax.validation.constraints.NotNull;

/**
 * 查询库存记录
 *
 * @author lidoudou
 * @date 2023/4/19 上午9:49
 */
@Data
public class ConsumablesStockRecordQueryForm extends PageParam {

    @ApiModelProperty("库存ID")
    @NotNull(message = "库存ID不能为空")
    private Long consumablesId;

    @ApiModelProperty("所属位置ID")
    private Long locationId;
}
