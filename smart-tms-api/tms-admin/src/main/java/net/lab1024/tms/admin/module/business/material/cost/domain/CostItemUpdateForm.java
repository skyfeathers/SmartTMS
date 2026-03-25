package net.lab1024.tms.admin.module.business.material.cost.domain;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author zhuoda
 * @Date 2022-07-18
 */
@Data
public class CostItemUpdateForm extends CostItemForm{

    @NotNull(message = "费用项id不能为空")
    private Long costItemId;

}
