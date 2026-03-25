package net.lab1024.tms.fixedasset.module.business.consumables.purchase.domain.form;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 易耗品采购主表
 *
 * @author lidoudou
 * @date 2023/4/12 下午4:02
 */
@Data
public class ConsumablesPurchaseUpdateForm extends ConsumablesPurchaseAddForm {

    @NotNull(message = "采购ID不能为空")
    private Long purchaseId;
}
