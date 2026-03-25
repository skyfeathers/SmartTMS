package net.lab1024.tms.admin.module.business.reportform.employee.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.admin.module.business.reportform.employee.domain.dto.EmployeeSalesTargetMonthDTO;
import org.hibernate.validator.constraints.Range;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 员工 业绩目标 更新
 *
 * @author Turbolisten
 * @date 2021/8/13 17:55
 */
@Data
public class EmployeeSalesTargetUpdateForm {

    @ApiModelProperty(value = "所属公司", hidden = true)
    @NotNull(message = "所属公司不能为空")
    private Long enterpriseId;

    @ApiModelProperty("员工id")
    @NotNull(message = "员工id不能为空")
    private Long employeeId;

    @ApiModelProperty("年份")
    @NotNull(message = "年份不能为空")
    @Range(min = 2021, max = 2099, message = "年份范围2021-2099")
    private Integer year;

    @ApiModelProperty("目标集合")
    @NotEmpty(message = "目标集合不能为空")
    @Size(max = 12, message = "最多12个月的目标")
    @Valid
    private List<EmployeeSalesTargetMonthDTO> monthList;

    @ApiModelProperty(hidden = true)
    private Long updateId;

    @ApiModelProperty(hidden = true)
    private String updateName;
}
