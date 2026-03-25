package net.lab1024.tms.common.module.business.carcost.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 油卡支出 Update Form
 *
 * @author zhaoxinyang
 * @date 2023/10/26 15:03
 */
@Data
public class CarCostUreaPayUpdateForm extends CarCostUreaPayAddForm {

    @NotNull(message = "油费支出ID不能为空")
    @ApiModelProperty("油费支出ID")
    private Long ureaPayId;

}
