package net.lab1024.tms.admin.module.business.driver.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.common.validator.enumeration.CheckEnum;
import net.lab1024.tms.common.constant.BusinessModeEnum;

import javax.validation.constraints.NotNull;

/***
 *
 * @author lidoudou
 * @date 2022/6/22 下午2:23
 */
@Data
public class DriverBusinessModeUpdateForm {

    @ApiModelProperty("司机ID")
    @NotNull(message = "司机ID不能为空")
    private Long driverId;

    @ApiModelPropertyEnum(value = BusinessModeEnum.class, desc = "业务类型")
    @CheckEnum(required = true, value = BusinessModeEnum.class, message = "业务类型不能为空")
    private Integer businessMode;
}
