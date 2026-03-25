package net.lab1024.tms.driver.module.business.driver.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/***
 *
 * @author lidoudou
 * @date 2022/6/22 下午2:23
 */
@Data
public class DriverUpdateForm extends DriverBaseForm {

    @ApiModelProperty("司机ID")
    @NotNull(message = "司机ID不能为空")
    private Long driverId;

    @ApiModelProperty(value = "所属公司", hidden = true)
    private Long enterpriseId;
}
