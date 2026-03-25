package net.lab1024.tms.admin.module.business.carcost.category.domain;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 更新费用分类 Form
 *
 * @author zhaoxinyang
 * @date 2023/10/25 09:20
 */
@Data
public class CarCostCategoryUpdateForm extends CarCostCategoryAddForm {

    @NotNull(message = "费用分类ID不能为空")
    private Long categoryId;

}