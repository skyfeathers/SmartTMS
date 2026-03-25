package net.lab1024.tms.admin.module.business.reportform.employee.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.domain.PageParam;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.module.business.waybill.constant.WaybillSettleModeEnum;

import java.time.LocalDate;
import java.util.List;

/**
 * @author lidoudou
 * @description:
 * @date 2023/6/29 上午11:34
 */
@Data
public class WaybillVehicleCountQueryForm extends PageParam {

    @ApiModelProperty("业务时间-月份开始时间")
    private LocalDate businessStartDate;

    @ApiModelProperty("业务时间-月份结束时间")
    private LocalDate businessEndDate;

    @ApiModelProperty("车辆ID列表")
    private List<Long> vehicleIdList;

    @ApiModelPropertyEnum(value = WaybillSettleModeEnum.class, desc = "结算方式")
    private Integer waybillSettleMode;

    @ApiModelProperty(value = "排除状态", hidden = true)
    private Integer excludeStatus;

    @ApiModelProperty(hidden = true)
    private Long enterpriseId;
}