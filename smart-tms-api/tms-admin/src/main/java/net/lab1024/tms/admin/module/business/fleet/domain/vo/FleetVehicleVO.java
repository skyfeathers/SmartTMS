package net.lab1024.tms.admin.module.business.fleet.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.admin.module.business.vehicle.domain.vo.VehicleVO;

@Data
public class FleetVehicleVO extends VehicleVO {

    @ApiModelProperty("主键ID")
    private Long fleetItemId;
}
