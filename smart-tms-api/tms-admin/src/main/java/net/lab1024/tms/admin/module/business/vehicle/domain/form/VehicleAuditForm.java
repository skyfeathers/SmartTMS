package net.lab1024.tms.admin.module.business.vehicle.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.common.validator.enumeration.CheckEnum;
import net.lab1024.tms.common.constant.AuditStatusEnum;

import javax.validation.constraints.NotNull;

/***
 * 车辆审核
 *
 * @author lidoudou
 * @date 2022/6/25 上午11:04
 */
@Data
public class VehicleAuditForm {

    @NotNull(message = "车辆不能为空")
    @ApiModelProperty("车辆ID")
    private Long vehicleId;

    @ApiModelProperty("审核状态")
    @NotNull(message = "审核结果不能为空")
    @CheckEnum(value = AuditStatusEnum.class, message = "审核结果错误", required = true)
    @ApiModelPropertyEnum(value = AuditStatusEnum.class, desc = "审核状态")
    private Integer auditStatus;

    @ApiModelProperty("备注")
    private String remark;
}
