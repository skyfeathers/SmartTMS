package net.lab1024.tms.driver.module.business.vehicle.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.driver.module.business.vehicle.domain.dto.VehicleBaseDTO;

import javax.validation.constraints.NotNull;

@Data
public class VehicleUpdateForm extends VehicleAddForm {

    @ApiModelProperty("车辆ID")
    @NotNull(message = "车辆不能为空")
    private Long vehicleId;

}
