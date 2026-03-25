package net.lab1024.tms.fixedasset.module.business.consumables.check.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 盘点
 *
 * @author lidoudou
 * @date 2023/4/14 下午5:06
 */
@Data
public class ConsumablesCheckForm {

    @NotNull(message = "盘点ID不能为空")
    private Long checkId;

    @ApiModelProperty("易耗品列表")
    @Size(min = 1, message = "易耗品列表不能为空")
    @Valid
    private List<ConsumablesCheckItemForm> itemList;
}
