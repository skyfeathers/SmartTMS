package net.lab1024.tms.admin.module.business.reportform.waybill.domain.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author lidoudou
 * @description:
 * @date 2023/6/29 上午11:38
 */
@Data
public class WaybillVehicleCountExportVO {

    @ApiModelProperty("车牌号")
    @Excel(name = "车辆", width = 20)
    private String vehicleNumber;

    @Excel(name = "司机", width = 20)
    private String driverName;

    @ApiModelProperty("运单数量")
    @Excel(name = "运单数量", width = 20)
    private Integer waybillCount;

    @Excel(name = "排名")
    private Integer rank;
}