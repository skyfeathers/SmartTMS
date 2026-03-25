package net.lab1024.tms.admin.module.business.maintenance.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.domain.PageParam;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

/**
 * 保养信息查询
 * @author zhaoxinyang
 * @date 2023/10/18 09:51
 */
@Data
public class MaintenanceQueryForm extends PageParam {

    @ApiModelProperty("车辆ID")
    private Long vehicleId;

    @ApiModelProperty("车辆ID列表")
    private List<Long> vehicleIdList;

    @ApiModelProperty(value = "保养时间-开始时间", example = "2022-01-01")
    private LocalDate startDate;

    @ApiModelProperty(value = "保养时间-结束时间", example = "2022-01-01")
    private LocalDate endDate;

    @ApiModelProperty(value = "删除状态", hidden = true)
    private Boolean deletedFlag;

    @ApiModelProperty(hidden = true)
    private Long enterpriseId;
}