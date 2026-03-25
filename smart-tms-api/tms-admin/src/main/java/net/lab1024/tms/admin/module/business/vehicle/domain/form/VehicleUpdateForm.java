package net.lab1024.tms.admin.module.business.vehicle.domain.form;

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
public class VehicleUpdateForm extends VehicleAddForm {

    @ApiModelProperty("车辆ID")
    @NotNull(message = "车辆不能为空")
    private Long vehicleId;
}
