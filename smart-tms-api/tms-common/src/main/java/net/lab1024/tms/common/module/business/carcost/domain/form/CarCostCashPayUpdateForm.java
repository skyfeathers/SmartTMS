package net.lab1024.tms.common.module.business.carcost.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 现金支出 Update Form
 *
 * @author zhaoxinyang
 * @date 2023/10/25 11:14
 */
@Data
public class CarCostCashPayUpdateForm extends CarCostCashPayAddForm {

    @ApiModelProperty("现金支出表ID")
    @NotNull(message = "现金支出表ID不能为空")
    private Long cashPayId;

}