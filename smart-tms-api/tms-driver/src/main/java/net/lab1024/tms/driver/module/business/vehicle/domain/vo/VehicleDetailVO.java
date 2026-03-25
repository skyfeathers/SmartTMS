package net.lab1024.tms.driver.module.business.vehicle.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.constant.BusinessModeEnum;
import net.lab1024.tms.common.module.support.datatracer.annoation.DataTracerFieldDoc;

/***
 * 车辆详情
 *
 * @author lidoudou
 * @date 2022/6/25 下午2:20
 */
@Data
public class VehicleDetailVO extends VehicleVO {

    @ApiModelPropertyEnum(value = BusinessModeEnum.class, desc = "经营方式")
    @DataTracerFieldDoc("经营方式")
    private Integer businessMode;

    @ApiModelProperty("经营方式描述")
    @DataTracerFieldDoc("经营方式描述")
    private String businessModeDesc;

    @ApiModelProperty("车辆能源类型名称")
    private String vehicleEnergyTypeName;

    @ApiModelProperty("挂车车号")
    @DataTracerFieldDoc("挂车车号")
    private String bracketNo;

    @ApiModelProperty("车牌颜色")
    @DataTracerFieldDoc("车牌颜色")
    private String vehiclePlateColor;

    @ApiModelProperty("审核状态描述")
    @DataTracerFieldDoc("审核状态描述")
    private String auditStatusDesc;
}
