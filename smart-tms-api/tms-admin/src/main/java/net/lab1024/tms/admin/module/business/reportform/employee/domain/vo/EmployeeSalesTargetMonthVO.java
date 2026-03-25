package net.lab1024.tms.admin.module.business.reportform.employee.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.admin.module.business.reportform.employee.domain.dto.EmployeeSalesTargetMonthDTO;

/**
 * 员工 业绩目标 月份
 *
 * @author Turbolisten
 * @date 2021/8/14 16:00
 */
@Data
public class EmployeeSalesTargetMonthVO extends EmployeeSalesTargetMonthDTO {

    @ApiModelProperty(value = "年份")
    private Integer year;

}
