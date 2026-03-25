package net.lab1024.tms.common.module.business.carcost.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 自有车-现金收入 UPDATE Form
 *
 * @author zhaoxinyang
 * @date 2023/10/24 08:58
 */
@Data
public class CarCostCashReceiveUpdateForm extends CarCostCashReceiveAddForm {

    @ApiModelProperty("现金收入表ID")
    @NotNull(message = "现金收入表ID不能为空")
    private Long cashReceiveId;

}