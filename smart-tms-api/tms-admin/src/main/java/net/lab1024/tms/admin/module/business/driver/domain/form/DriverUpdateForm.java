package net.lab1024.tms.admin.module.business.driver.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/***
 *
 * @author lidoudou
 * @date 2022/6/22 下午2:23
 */
@Data
public class DriverUpdateForm extends DriverAddForm {

    @ApiModelProperty("司机ID")
    @NotNull(message = "司机ID不能为空")
    private Long driverId;
}
