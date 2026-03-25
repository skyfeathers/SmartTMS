package net.lab1024.tms.fixedasset.module.business.consumables.requisition.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.fixedasset.module.business.consumables.stock.domain.vo.ConsumablesVO;

/**
 * 采购
 *
 * @author lidoudou
 * @date 2023/4/14 下午4:06
 */
@Data
public class ConsumablesRequisitionItemVO extends ConsumablesVO {

    @ApiModelProperty("领用数量")
    private Integer count;
}
