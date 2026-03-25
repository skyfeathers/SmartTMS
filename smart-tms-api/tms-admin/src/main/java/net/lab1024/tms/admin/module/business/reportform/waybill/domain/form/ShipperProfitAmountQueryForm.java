package net.lab1024.tms.admin.module.business.reportform.waybill.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.module.business.waybill.constant.WaybillTransportModeEnum;

import java.time.LocalDate;
import java.util.Collection;

/**
 * @author lidoudou
 * @date 2023/3/4 下午3:22
 */
@Data
public class ShipperProfitAmountQueryForm {

    @ApiModelProperty("业务时间-月份开始时间")
    private LocalDate businessStartDate;

    @ApiModelProperty("业务时间-月份结束时间")
    private LocalDate businessEndDate;

    @ApiModelProperty("所属公司")
    private Long enterpriseId;

    @ApiModelProperty(value = "货主ID列表", hidden = true)
    private Collection<Long> shipperIdList;

    @ApiModelProperty(value = "排除状态", hidden = true)
    private Integer excludeStatus;

    @ApiModelPropertyEnum(value = WaybillTransportModeEnum.class, desc = "运输方式")
    private Integer transportMode;

}
