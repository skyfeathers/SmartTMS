package net.lab1024.tms.admin.module.business.oa.cost.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

/**
 * 新建 - 费用申请
 *
 * @author lidoudou
 * @date 2023/3/29 下午4:58
 */
@Data
public class CostApplyAddForm {

    @ApiModelProperty("申请日期")
    @NotNull(message = "申请日期不能为空")
    private LocalDate applyDate;

    @ApiModelProperty("所属企业")
    private Long enterpriseId;

    @ApiModelProperty("所属部门")
    @NotNull(message = "所属部门不能为空")
    private Long departmentId;

    @ApiModelProperty("申请人")
    @NotNull(message = "申请人不能为空")
    private Long applyUserId;

    @ApiModelProperty("备注")
    private String remark;

    @Valid
    @ApiModelProperty("报销项目列表")
    @NotNull(message = "报销项目明细不能为空")
    private List<CostApplyItemAddForm> itemList;
}