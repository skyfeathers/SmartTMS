package net.lab1024.tms.common.module.business.carcost.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * ETC支出 Update Form
 *
 * @author zhaoxinyang
 * @date 2023/10/26 10:00
 */
@Data
public class CarCostEtcPayUpdateForm extends CarCostEtcPayAddForm {

    @NotNull(message = "ETC支出ID不能为空")
    @ApiModelProperty("ETC支出ID")
    private Long etcPayId;

}
