package net.lab1024.tms.fixedasset.module.business.consumables.requisition.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.domain.PageParam;

import java.time.LocalDate;

/**
 * 查询条件
 *
 * @author lidoudou
 * @date 2023/4/14 上午9:45
 */
@Data
public class ConsumablesRequisitionQueryForm extends PageParam {

    @ApiModelProperty(value = "使用时间 - 开始时间")
    private LocalDate startTime;

    @ApiModelProperty(value = "使用时间 - 结束时间")
    private LocalDate endTime;

    @ApiModelProperty(value = "关键字")
    private String keywords;

    @ApiModelProperty(value = "所属公司")
    private Long enterpriseId;

    @ApiModelProperty(value = "所属部门")
    private Long departmentId;

    @ApiModelProperty(value = "所属位置")
    private Long locationId;
}