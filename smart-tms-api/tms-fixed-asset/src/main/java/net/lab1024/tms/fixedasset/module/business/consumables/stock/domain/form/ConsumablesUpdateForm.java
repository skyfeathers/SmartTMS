package net.lab1024.tms.fixedasset.module.business.consumables.stock.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 易耗品更新
 *
 * @author lidoudou
 * @date 2023/4/12 上午10:05
 */
@Data
public class ConsumablesUpdateForm extends ConsumablesAddForm {

    @ApiModelProperty("自增ID")
    @NotNull(message = "ID不能为空")
    private Long consumablesId;
}
