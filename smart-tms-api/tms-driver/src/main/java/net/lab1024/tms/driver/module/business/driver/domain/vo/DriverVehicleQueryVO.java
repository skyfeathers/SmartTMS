package net.lab1024.tms.driver.module.business.driver.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.constant.AuditStatusEnum;

/**
 * 根据司机查询车辆信息
 *
 * @author: zhaoxinyang
 * @date: 2023/08/28 18:16
 */
@Data
public class DriverVehicleQueryVO {

    @ApiModelProperty("车辆ID")
    private Long vehicleId;

    @ApiModelProperty("车牌号")
    private String vehicleNumber;

    @ApiModelPropertyEnum(value = AuditStatusEnum.class,desc = "审核状态")
    private Integer auditStatus;

    @ApiModelProperty("挂车车号")
    private Long bracketId;

    @ApiModelProperty("挂车车牌号")
    private String bracketNo;

    @ApiModelProperty("禁用状态")
    private Boolean disabledFlag;

}
