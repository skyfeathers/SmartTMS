package net.lab1024.tms.admin.module.business.oa.cost.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.domain.PageParam;

import java.time.LocalDate;

/**
 * 查询费用申请
 *
 * @author lidoudou
 * @date 2023/3/29 下午4:46
 */
@Data
public class CostApplyQueryForm extends PageParam {

    @ApiModelProperty(value = "关键字")
    private String keywords;

    @ApiModelProperty(value = "业务日期")
    private LocalDate applyDateBegin;

    @ApiModelProperty(value = "业务日期")
    private LocalDate applyDateEnd;

    @ApiModelProperty("所属部门")
    private Long departmentId;

    @ApiModelProperty(value = "企业id", hidden = true)
    private Long enterpriseId;
}