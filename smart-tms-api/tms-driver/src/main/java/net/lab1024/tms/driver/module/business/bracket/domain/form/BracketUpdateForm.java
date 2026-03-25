package net.lab1024.tms.driver.module.business.bracket.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/***
 * 车辆修改
 *
 * @author lidoudou
 * @date 2022/6/25 上午10:51
 */
@Data
public class BracketUpdateForm extends BracketAddForm {

    @ApiModelProperty("挂车ID")
    @NotNull(message = "挂车不能为空")
    private Long bracketId;

}
