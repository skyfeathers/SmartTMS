package net.lab1024.tms.admin.module.business.reportform.carcost.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

/**
 * 自有车月报表查询
 *
 * @author lidoudou
 * @date 2023/4/8 上午8:59
 */
@Data
public class CarCostMonthStatisticQueryForm {

    @ApiModelProperty("车辆ID")
    private List<Long> vehicleIdList;


    @ApiModelProperty("开始时间")
    @NotNull(message = "请选择开始时间")
    private LocalDate startTime;

    @ApiModelProperty("结束时间")
    @NotNull(message = "请选择结束时间")
    private LocalDate endTime;

    @ApiModelProperty(value = "删除标识", hidden = true)
    private Boolean deletedFlag;

    @ApiModelProperty("所属公司")
    private Long enterpriseId;
}