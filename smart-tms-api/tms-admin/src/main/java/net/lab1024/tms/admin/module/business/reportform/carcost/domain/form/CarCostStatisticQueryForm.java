package net.lab1024.tms.admin.module.business.reportform.carcost.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.domain.PageParam;

import java.time.LocalDate;

/**
 * 自有车报表查询
 *
 * @author lidoudou
 * @date 2023/4/8 上午8:59
 */
@Data
public class CarCostStatisticQueryForm extends PageParam {

    @ApiModelProperty("车辆ID")
    private Long vehicleId;



    @ApiModelProperty("开始时间")
    private LocalDate startTime;

    @ApiModelProperty("结束时间")
    private LocalDate endTime;

    @ApiModelProperty(value = "删除标识", hidden = true)
    private Boolean deletedFlag;

    @ApiModelProperty(value = "所属公司",hidden = true)
    private Long enterpriseId;
}