package net.lab1024.tms.driver.module.business.vehicle.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.constant.AuditStatusEnum;
import net.lab1024.tms.common.module.support.datatracer.annoation.DataTracerFieldBoolean;
import net.lab1024.tms.common.module.support.datatracer.annoation.DataTracerFieldDoc;
import net.lab1024.tms.common.module.support.datatracer.annoation.DataTracerFieldEnum;
import net.lab1024.tms.driver.module.business.vehicle.domain.dto.VehicleBaseDTO;

/***
 * 车辆列表
 *
 * @author lidoudou
 * @date 2022/6/25 下午2:02
 */
@Data
public class VehicleVO extends VehicleBaseDTO {

    @ApiModelProperty("车辆ID")
    private Long vehicleId;

    @ApiModelProperty("状态")
    @DataTracerFieldDoc("禁用状态")
    @DataTracerFieldBoolean
    private Boolean disabledFlag;

    @DataTracerFieldDoc("审核状态")
    @DataTracerFieldEnum(enumClass = AuditStatusEnum.class)
    @ApiModelPropertyEnum(value = AuditStatusEnum.class,desc = "审核状态")
    private Integer auditStatus;

}
