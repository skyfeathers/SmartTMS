package net.lab1024.tms.admin.module.business.fleet.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/***
 * @author lidoudou
 * @date 2022/6/27 下午4:02
 */
@Data
public class FleetUpdateForm extends FleetAddForm {

    @ApiModelProperty("车队ID")
    @NotNull(message = "车队ID不能为空")
    private Long fleetId;
}
