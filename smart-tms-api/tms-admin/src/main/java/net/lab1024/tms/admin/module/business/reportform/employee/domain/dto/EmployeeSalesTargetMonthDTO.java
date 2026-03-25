package net.lab1024.tms.admin.module.business.reportform.employee.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 员工 业绩目标 月份
 *
 * @author Turbolisten
 * @date 2021/8/14 16:00
 */
@Data
public class EmployeeSalesTargetMonthDTO {

    @ApiModelProperty(value = "月份", example = "10")
    @NotNull(message = "月份不能为空")
    @Range(min = 1, max = 12, message = "月份1-12")
    private Integer month;

    @ApiModelProperty("目标")
    @NotNull(message = "目标不能为空")
    @DecimalMin(value = "0", message = "目标最小0")
    @DecimalMax(value = "99999999", message = "目标最大99999999")
    private BigDecimal target;

    @ApiModelProperty("备注|可选")
    @Length(max = 200, message = "备注最多200字符")
    private String remark;
}
