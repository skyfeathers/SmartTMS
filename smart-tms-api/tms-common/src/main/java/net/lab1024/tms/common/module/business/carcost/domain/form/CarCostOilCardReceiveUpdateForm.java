package net.lab1024.tms.common.module.business.carcost.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 自有车-油卡收入 UPDATE Form
 *
 * @author zhaoxinyang
 * @date 2023/10/24 11:22
 */
@Data
public class CarCostOilCardReceiveUpdateForm extends CarCostOilCardReceiveAddForm {

    @ApiModelProperty("油卡收入表ID")
    @NotNull(message = "油卡收入表ID不能为空")
    private Long oilCardReceiveId;

}