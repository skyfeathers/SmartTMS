package net.lab1024.tms.admin.module.business.reportform.employee.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 员工 业绩目标 展示 vo
 *
 * @author Turbolisten
 * @date 2021/8/13 17:55
 */
@Data
public class EmployeeSalesTargetVO {

    @ApiModelProperty("员工id")
    private Long employeeId;

    @ApiModelProperty("员工名称")
    private String employeeName;

    @ApiModelProperty("目标列表")
    private List<EmployeeSalesTargetMonthVO> monthList;

    private String updateName;

    private LocalDateTime updateTime;

    private LocalDateTime createTime;
}
