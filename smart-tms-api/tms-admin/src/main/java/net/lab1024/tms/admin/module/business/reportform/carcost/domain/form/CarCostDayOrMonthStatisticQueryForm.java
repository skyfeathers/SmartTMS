package net.lab1024.tms.admin.module.business.reportform.carcost.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * 自有车日/月费用统计
 *
 * @author zhaoxinyang
 * @date 2024/10/9 9:12
 */
@Data
public class CarCostDayOrMonthStatisticQueryForm {

    @ApiModelProperty("关键字")
    private String keyword;

    @ApiModelProperty("开始时间")
    @NotNull(message = "请选择开始时间")
    private LocalDate startTime;

    @ApiModelProperty("结束时间")
    @NotNull(message = "请选择结束时间")
    private LocalDate endTime;

    @ApiModelProperty(value = "异常标识")
    private Boolean exceptionFlag;

    @ApiModelProperty(value = "所属公司",hidden = true)
    private Long enterpriseId;

}
