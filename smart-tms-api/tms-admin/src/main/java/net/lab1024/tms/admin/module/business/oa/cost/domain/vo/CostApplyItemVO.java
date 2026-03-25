package net.lab1024.tms.admin.module.business.oa.cost.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/***
 * 申请的费用项目详情
 *
 * @author lidoudou
 * @date 2023/3/29 下午5:02
 */
@Data
public class CostApplyItemVO {

    private Long applyItemId;

    @ApiModelProperty("主表ID")
    private Long applyId;

    @ApiModelProperty("报销项目")
    private String applyItemName;

    @ApiModelProperty("报销金额")
    private BigDecimal applyAmount;

    @ApiModelProperty("备注")
    private String remark;
}
