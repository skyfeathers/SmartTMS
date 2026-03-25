package net.lab1024.tms.admin.module.business.reportform.waybill.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author lidoudou
 * @description:
 * @date 2023/6/29 上午11:38
 */
@Data
public class WaybillVehicleCountVO {

    @ApiModelProperty("车辆ID")
    private Long vehicleId;

    @ApiModelProperty("车牌号")
    private String vehicleNumber;

    @ApiModelProperty("运单数量")
    private Integer waybillCount;

    @ApiModelProperty("司机姓名")
    private List<String> driverNameList;
}