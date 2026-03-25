package net.lab1024.tms.driver.module.business.vehicle.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.constant.BusinessModeEnum;
import net.lab1024.tms.common.module.support.datatracer.annoation.DataTracerFieldDoc;
import net.lab1024.tms.common.module.support.datatracer.annoation.DataTracerFieldEnum;
import net.lab1024.tms.driver.module.business.vehicle.domain.dto.VehicleBaseDTO;
import org.hibernate.validator.constraints.Length;

@Data
public class VehicleAddForm extends VehicleBaseDTO {

    @ApiModelProperty("负责人")
    private Long managerId;

    @ApiModelProperty("司机")
    private Long driverId;

    @ApiModelProperty(value = "operatorId",hidden = true)
    private Long operatorId;

    @ApiModelProperty(value = "operatorName",hidden = true)
    private String operatorName;

    @ApiModelProperty(value = "operatorType",hidden = true)
    private Integer operatorType;

    @ApiModelProperty(value = "operatorTypeDesc",hidden = true)
    private String operatorTypeDesc;

}
