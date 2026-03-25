package net.lab1024.tms.admin.module.business.waybill.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.common.validator.enumeration.CheckEnum;
import net.lab1024.tms.common.module.business.waybill.constant.WaybillSettleModeEnum;
import net.lab1024.tms.common.module.business.waybill.constant.WaybillSettleTypeEnum;

import javax.validation.constraints.NotNull;

/**
 * @author yandy
 * @description:
 * @date 2022/8/12 4:42 下午
 */
@Data
public class WaybillSettleTypeForm {

    @ApiModelPropertyEnum(value = WaybillSettleModeEnum.class,desc = "结算方式")
    @CheckEnum(value = WaybillSettleModeEnum.class,required = true,message = "结算方式错误")
    private Integer settleMode;

    @ApiModelProperty("车辆id")
    @NotNull(message = "车辆不能为空")
    private Long vehicleId;

    @ApiModelProperty("司机id")
    @NotNull(message = "司机不能为空")
    private Long driverId;

    @ApiModelProperty("车队id")
    private Long fleetId;
}