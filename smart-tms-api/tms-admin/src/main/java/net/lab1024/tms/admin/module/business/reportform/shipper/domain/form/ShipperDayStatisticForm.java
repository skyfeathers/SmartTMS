package net.lab1024.tms.admin.module.business.reportform.shipper.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.domain.PageParam;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.module.business.order.constant.OrderStatusEnum;
import net.lab1024.tms.common.module.business.waybill.constant.WaybillStatusEnum;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

/**
 * 货主日报表
 *
 * @author lidoudou
 * @date 2023/3/7 上午11:13
 */
@Data
public class ShipperDayStatisticForm extends PageParam {

    @ApiModelProperty("开始日期")
    @NotNull(message = "开始日期不能为空")
    private LocalDate startDate;

    @ApiModelProperty("结束日期")
    @NotNull(message = "结束日期不能为空")
    private LocalDate endDate;

    @ApiModelProperty("货主ID | 可选")
    private Long shipperId;

    @ApiModelProperty("负责人ID")
    private List<Long> managerIdList;

    @ApiModelProperty(value = "账期", hidden = true)
    private LocalDate accountPeriodDate;

    @ApiModelProperty("是否核销")
    private Boolean verificationFlag;

    @ApiModelProperty(value = "货主ID列表", hidden = true)
    private Collection<Long> shipperIdList;

    @ApiModelProperty(value = "企业id", hidden = true)
    private Long enterpriseId;

    @ApiModelPropertyEnum(value = WaybillStatusEnum.class, desc = "排除的运单状态", hidden = true)
    private Integer excludeStatus;

    @ApiModelPropertyEnum(value = OrderStatusEnum.class, desc = "订单状态", hidden = true)
    private Integer orderStatus;

}
