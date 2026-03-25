package net.lab1024.tms.fixedasset.module.business.consumables.requisition.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 易耗品列表
 *
 * @author lidoudou
 * @date 2023/4/14 下午2:27
 */
@Data
public class ConsumablesRequisitionItemAddForm {

    @ApiModelProperty("易耗品ID")
    @NotNull(message = "易耗品ID不能为空")
    private Long consumablesId;

    @ApiModelProperty("领取数量")
    @NotNull(message = "领取数量不能为空")
    private Integer count;
}
