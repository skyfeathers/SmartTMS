package net.lab1024.tms.admin.module.business.waybill.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class WaybillDriverVehicleBO {

    @ApiModelProperty("车牌号")
    private String vehicleNumber;

    @ApiModelProperty("司机姓名")
    private String driverName;

    @ApiModelProperty("司机手机号")
    private String telephone;

    @ApiModelProperty("司机身份证号")
    private String drivingLicense;
}
