package net.lab1024.tms.fixedasset.module.business.consumables.requisition.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

/**
 * 新增领用
 *
 * @author lidoudou
 * @date 2023/4/14 上午9:43
 */
@Data
public class ConsumablesRequisitionAddForm {

    @ApiModelProperty("日期")
    @NotNull(message = "日期不能为空")
    private LocalDate useTime;

    @ApiModelProperty("公司")
    private Long enterpriseId;

    @ApiModelProperty("所属位置")
    @NotNull(message = "所属位置不能为空")
    private Long locationId;

    @ApiModelProperty("领用部门")
    @NotNull(message = "领用部门不能为空")
    private Long departmentId;

    @ApiModelProperty("领用人")
    @NotNull(message = "领用人不能为空")
    private Long userId;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("易耗品ID列表")
    @Size(min = 1, message = "至少选择一个易耗品")
    @Valid
    private List<ConsumablesRequisitionItemAddForm> itemList;
}