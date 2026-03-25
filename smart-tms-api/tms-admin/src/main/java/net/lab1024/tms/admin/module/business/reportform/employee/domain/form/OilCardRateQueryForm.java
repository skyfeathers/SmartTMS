package net.lab1024.tms.admin.module.business.reportform.employee.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Collection;

/**
 * 外调车油卡查询
 *
 * @author lidoudou
 * @date 2023/3/9 上午10:31
 */
@Data
public class OilCardRateQueryForm {

    @ApiModelProperty("月份")
    @NotNull(message = "请选择月份")
    private LocalDate queryDate;

    @ApiModelProperty("员工id|可选")
    private Long employeeId;

    @ApiModelProperty(hidden = true,value = "结算类型")
    private Integer settleMode;

    @ApiModelProperty(value = "员工ID列表", hidden = true)
    private Collection<Long> employeeIdList;

    @ApiModelProperty(hidden = true)
    private Integer excludeStatus;

    @ApiModelProperty(hidden = true)
    private Long enterpriseId;
}
