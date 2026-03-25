package net.lab1024.tms.admin.module.business.reportform.employee.domain.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 根据调度查询运单
 *
 * @author zhaoxinyang
 * @date 2023/12/11
 */
@Data
public class EmployeeScheduleWaybillDTO {

    @ApiModelProperty("运单ID")
    private Long waybillId;

    @ApiModelProperty("所属公司ID")
    private Long enterpriseId;

    @ApiModelProperty("应付金额")
    private BigDecimal payableAmount;

    @ApiModelProperty("调度ID")
    private Long scheduleId;

}
