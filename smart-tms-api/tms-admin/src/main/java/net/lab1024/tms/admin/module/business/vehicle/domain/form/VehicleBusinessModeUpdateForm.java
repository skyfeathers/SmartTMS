package net.lab1024.tms.admin.module.business.vehicle.domain.form;

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
public class VehicleBusinessModeUpdateForm {

    @ApiModelProperty("车辆ID")
    @NotNull(message = "车辆ID不能为空")
    private Long vehicleId;

    @ApiModelPropertyEnum(value = BusinessModeEnum.class, desc = "业务类型")
    @CheckEnum(required = true, value = BusinessModeEnum.class, message = "业务类型不能为空")
    private Integer businessMode;
}
