package net.lab1024.tms.fixedasset.module.business.consumables.check.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 盘点耗材
 *
 * @author lidoudou
 * @date 2023/4/14 下午5:05
 */
@Data
public class ConsumablesCheckItemForm {

    @NotNull(message = "盘点对象不能为空")
    private Long itemId;

    @ApiModelProperty("盘点数量")
    @NotNull(message = "盘点数量不能为空")
    private Integer count;

    @NotNull(message = "盘点状态不能为空")
    private Integer status;

    private String remark;
}
