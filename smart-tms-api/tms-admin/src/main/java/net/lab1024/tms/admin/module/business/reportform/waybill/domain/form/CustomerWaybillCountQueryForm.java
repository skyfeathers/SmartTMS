package net.lab1024.tms.admin.module.business.reportform.waybill.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

/**
 * @author zhaoxinyang
 * @description: 客户日推进查询
 * @date 2023/09/26
 */
@Data
public class CustomerWaybillCountQueryForm {

    @ApiModelProperty("客服ID")
    private List<Long> employeeIdList;

    @ApiModelProperty("运输模式")
    private List<Integer> tripTypeList;

    @ApiModelProperty(value = "装卸货-开始时间", example = "2023-01-01")
    @NotNull(message = "装卸货开始时间不能为空")
    private LocalDate startLoadTime;

    @ApiModelProperty(value = "装卸货-截止时间", example = "2023-01-01")
    @NotNull(message = "装卸货截止时间不能为空")
    private LocalDate endLoadTime;

    @ApiModelProperty(hidden = true)
    private Integer excludeOrderStatus;

    @ApiModelProperty(hidden = true)
    private Integer excludeWaybillStatus;

    @ApiModelProperty(hidden = true)
    private Long enterpriseId;
}
