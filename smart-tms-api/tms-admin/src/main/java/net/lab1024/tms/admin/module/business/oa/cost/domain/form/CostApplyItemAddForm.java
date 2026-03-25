package net.lab1024.tms.admin.module.business.oa.cost.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 新建 - 费用申请
 *
 * @author lidoudou
 * @date 2023/3/29 下午4:58
 */
@Data
public class CostApplyItemAddForm {

    @ApiModelProperty("费用名称")
    @NotBlank(message = "报销项目不能为空")
    private String applyItemName;

    @ApiModelProperty("报销金额")
    @NotNull(message = "报销金额不能为空")
    private String remark;

    private BigDecimal applyAmount;
}