package net.lab1024.tms.admin.module.business.drivervehicle.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.module.support.fieldencrypt.FieldEncrypt;

/**
 * 司机车辆关联
 *
 * @author lihaifan
 * @date 2022/7/11 11:55
 */
@Data
public class DriverVehicleVO {

    @ApiModelProperty("司机ID")
    private Long driverId;

    @ApiModelProperty("司机名称")
    @FieldEncrypt
    private String driverName;

    @ApiModelProperty("司机手机号")
    @FieldEncrypt
    private String telephone;

    @ApiModelProperty("车辆ID")
    private Long vehicleId;

    @ApiModelProperty("车牌号")
    @FieldEncrypt
    private String vehicleNumber;
}
