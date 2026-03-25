package net.lab1024.tms.admin.module.business.reportform.home.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *
 * @author yandy
 * @description:
 * @date 2026/3/17 上午10:34
 */
@Data
public class HomeNumDTO {

    @ApiModelProperty("司机数量")
    private Integer driverNum;

    @ApiModelProperty("车辆数量")
    private Integer vehicleNum;

    @ApiModelProperty("订单数量")
    private Integer orderNum;

    @ApiModelProperty("运单数量")
    private Integer waybillNum;

}