package net.lab1024.tms.admin.module.business.waybill.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author yandy
 * @description:
 * @date 2022/8/14 11:18 下午
 */
@Data
public class WaybillCostItemForm {

    @ApiModelProperty("运单费用项id")
    @NotNull(message = "运单费用项id不能为空")
    private Long waybillCostId;

    @ApiModelProperty("费用")
    @NotNull(message = "应付费用不能为空")
    private BigDecimal costAmount;

    @ApiModelProperty("备注")
    private String remark;
}