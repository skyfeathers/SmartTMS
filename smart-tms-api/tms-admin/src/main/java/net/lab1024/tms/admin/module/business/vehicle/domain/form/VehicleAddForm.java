package net.lab1024.tms.admin.module.business.vehicle.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.admin.module.business.vehicle.domain.dto.VehicleBaseDTO;

import java.util.List;

/***
 * 车辆添加
 *
 * @author lidoudou
 * @date 2022/6/25 上午10:50
 */
@Data
public class VehicleAddForm extends VehicleBaseDTO {

    @ApiModelProperty("司机ID列表")
    private List<Long> driverIdList;

    @ApiModelProperty(value = "operatorId", hidden = true)
    private Long operatorId;

    @ApiModelProperty(value = "operatorName", hidden = true)
    private String operatorName;

    @ApiModelProperty(value = "operatorType", hidden = true)
    private Integer operatorType;

    @ApiModelProperty(value = "operatorTypeDesc", hidden = true)
    private String operatorTypeDesc;
}
