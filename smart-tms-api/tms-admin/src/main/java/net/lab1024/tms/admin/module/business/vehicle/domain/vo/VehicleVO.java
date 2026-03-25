package net.lab1024.tms.admin.module.business.vehicle.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.admin.module.business.drivervehicle.domain.DriverVehicleVO;
import net.lab1024.tms.admin.module.business.vehicle.domain.dto.VehicleBaseDTO;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.constant.AuditStatusEnum;
import net.lab1024.tms.common.module.support.datatracer.annoation.DataTracerFieldBoolean;
import net.lab1024.tms.common.module.support.datatracer.annoation.DataTracerFieldDoc;
import net.lab1024.tms.common.module.support.datatracer.annoation.DataTracerFieldEnum;
import net.lab1024.tms.common.module.support.fieldencrypt.FieldEncrypt;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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

    @ApiModelProperty("下次保养时间")
    private LocalDate nextMaintenanceDate;

    @ApiModelProperty("下次保养里程")
    private BigDecimal nextMaintenanceMileage;

    @ApiModelProperty("状态")
    @DataTracerFieldDoc("禁用状态")
    @DataTracerFieldBoolean
    private Boolean disabledFlag;

    @DataTracerFieldDoc("审核状态")
    @DataTracerFieldEnum(enumClass = AuditStatusEnum.class)
    @ApiModelPropertyEnum(value = AuditStatusEnum.class,desc = "审核状态")
    private Integer auditStatus;

    @FieldEncrypt
    @ApiModelProperty("挂车车牌号")
    @DataTracerFieldDoc("挂车车牌号")
    private String bracketNo;

    @ApiModelProperty("负责人姓名")
    private String managerName;

    @ApiModelProperty("所属企业名称")
    private String enterpriseName;

    @ApiModelProperty("创建人名称")
    private String createUserName;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("商业险到期时间")
    @DataTracerFieldDoc("商业险到期时间")
    private LocalDate commercialExpireTime;

    @ApiModelProperty("交强险到期时间")
    @DataTracerFieldDoc("交强险到期时间")
    private LocalDate compulsoryTrafficExpireTime;

    @ApiModelProperty("关联司机信息")
    private List<DriverVehicleVO> driverVehicleVOList;

}
