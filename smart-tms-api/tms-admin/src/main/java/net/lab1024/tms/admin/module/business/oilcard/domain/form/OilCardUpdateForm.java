package net.lab1024.tms.admin.module.business.oilcard.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 编辑
 *
 * @author lidoudou
 * @date 2022/6/30 上午10:54
 */
@Data
public class OilCardUpdateForm extends OilCardAddForm {

    @ApiModelProperty("油卡ID")
    @NotNull(message = "油卡ID不能为空")
    private Long oilCardId;

}
