package net.lab1024.tms.admin.module.business.vehicle.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.admin.module.business.drivervehicle.domain.DriverVehicleVO;
import net.lab1024.tms.admin.module.business.insurance.domain.vo.InsuranceVO;
import net.lab1024.tms.common.module.support.datatracer.annoation.DataTracerFieldDoc;

import java.util.List;

/***
 * 车辆详情
 *
 * @author lidoudou
 * @date 2022/6/25 下午2:20
 */
@Data
public class VehicleDetailVO extends VehicleVO {

    @ApiModelProperty("车辆能源类型名称")
    private String vehicleEnergyTypeName;

    @ApiModelProperty("挂车车号")
    @DataTracerFieldDoc("挂车车号")
    private String bracketNo;

    @ApiModelProperty("保险列表")
    private List<InsuranceVO> insuranceList;

    @ApiModelProperty("司机车辆关联")
    private List<DriverVehicleVO> driverVehicleList;
}
