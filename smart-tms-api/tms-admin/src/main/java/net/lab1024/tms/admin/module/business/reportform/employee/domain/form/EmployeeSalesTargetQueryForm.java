package net.lab1024.tms.admin.module.business.reportform.employee.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;

/**
 * 员工 业绩目标 查询
 *
 * @author Turbolisten
 * @date 2021/8/13 17:55
 */
@Data
public class EmployeeSalesTargetQueryForm {

    @ApiModelProperty("年份")
    @NotNull(message = "请选择年份")
    @Range(min = 2021, max = 2099, message = "年份范围2021-2099")
    private Integer year;

    @ApiModelProperty("员工id|可选")
    private Long employeeId;

    @ApiModelProperty(value = "员工idList")
    private List<Long> employeeIdList;

    @ApiModelProperty(hidden = true)
    private Long enterpriseId;

}
