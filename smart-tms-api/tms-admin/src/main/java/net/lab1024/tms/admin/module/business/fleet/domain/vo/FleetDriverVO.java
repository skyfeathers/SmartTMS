package net.lab1024.tms.admin.module.business.fleet.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.admin.module.business.driver.domain.vo.DriverVO;

@Data
public class FleetDriverVO extends DriverVO {

    @ApiModelProperty("主键ID")
    private Long fleetItemId;
}
