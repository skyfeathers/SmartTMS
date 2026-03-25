package net.lab1024.tms.admin.module.business.reportform.employee.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Collection;

/**
 * 查询员工销售情况
 *
 * @author lidoudou
 * @date 2023/3/7 上午11:13
 */
@Data
public class EmployeeSalesQueryForm {

    @ApiModelProperty("月份")
    @NotNull(message = "请选择月份")
    private LocalDate queryDate;

    @ApiModelProperty("员工id|可选")
    private Long employeeId;

    @ApiModelProperty("部门ID | 可选")
    private Long departmentId;

    @ApiModelProperty(value = "员工ID列表", hidden = true)
    private Collection<Long> employeeIdList;

    @ApiModelProperty(value = "排除的运单状态", hidden = true)
    private Integer excludeStatus;

    @ApiModelProperty(hidden = true)
    private Long enterpriseId;

}
