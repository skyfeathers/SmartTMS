package net.lab1024.tms.admin.module.business.reportform.employee.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 根据调度查询订单
 *
 * @author zhaoxinyang
 * @date 2023/12/11
 */
@Data
public class EmployeeScheduleOrderDTO {

    @ApiModelProperty("订单ID")
    private Long orderId;

    @ApiModelProperty("调度ID")
    private Long scheduleId;

}
