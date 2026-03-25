package net.lab1024.tms.admin.module.business.reportform.employee.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.module.business.waybill.constant.WaybillStatusEnum;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

/**
 * 调度日报表
 *
 * @author zhaoxinyang
 * @date 2023/12/12 08:31
 */
@Data
public class ScheduleDayStatisticQueryForm {

    @ApiModelProperty("开始日期")
    @NotNull(message = "开始日期不能为空")
    private LocalDate startDate;

    @ApiModelProperty("结束日期")
    @NotNull(message = "结束日期不能为空")
    private LocalDate endDate;

    @ApiModelProperty("部门ID | 可选")
    private Long departmentId;

    @ApiModelProperty("调度ID列表")
    private List<Long> scheduleIdList;

    @ApiModelProperty(value = "员工ID列表", hidden = true)
    private Collection<Long> employeeIdList;

    @ApiModelPropertyEnum(value = WaybillStatusEnum.class, desc = "排除的运单状态", hidden = true)
    private Integer excludeStatus;

    @ApiModelProperty(value = "分配状态", hidden = true)
    private Boolean scheduleFlag;

    @ApiModelProperty(value = "排除的订单状态", hidden = true)
    private Integer excludeOrderStatus;

    @ApiModelProperty(hidden = true)
    private Long enterpriseId;

}
