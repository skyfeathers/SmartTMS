package net.lab1024.tms.admin.module.business.reportform.employee.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.module.business.order.constant.OrderStatusEnum;
import net.lab1024.tms.common.module.business.waybill.constant.WaybillStatusEnum;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

/**
 * 销售应收统计
 *
 * @Author lidoudou
 * @Date 2023/6/29 上午9:07
 */
@Data
public class SalesReceiveAmountStatisticQueryForm {

    @ApiModelProperty("业务时间-月份开始时间")
    private LocalDate businessStartDate;

    @ApiModelProperty("业务时间-月份结束时间")
    private LocalDate businessEndDate;

    @ApiModelProperty("员工id|可选")
    private List<Long> employeeIdList;
    /**
     * 订单状态
     *
     * @see OrderStatusEnum
     */
    @ApiModelProperty(hidden = true)
    private Integer orderStatus;

    /**
     * 运单状态
     *
     * @see WaybillStatusEnum
     */
    @ApiModelProperty(hidden = true)
    private Integer waybillStatus;

    @ApiModelProperty(hidden = true)
    private Boolean deletedFlag;

    @ApiModelProperty(hidden = true)
    private Long enterpriseId;
}
