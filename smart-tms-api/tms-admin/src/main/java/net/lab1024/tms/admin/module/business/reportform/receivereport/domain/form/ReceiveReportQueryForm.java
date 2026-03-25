package net.lab1024.tms.admin.module.business.reportform.receivereport.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;

/**
 * @author lidoudou
 * @date 2023/3/3 下午2:43
 */
@Data
public class ReceiveReportQueryForm {

    @ApiModelProperty("员工ID")
    private Long employeeId;

    @ApiModelProperty("开始时间")
    private LocalDate startTime;

    @ApiModelProperty("结束时间")
    private LocalDate endTime;
}
