package net.lab1024.tms.admin.module.business.pay.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author yandy
 * @description:
 * @date 2022/8/15 11:53 上午
 */
@Data
public class PayOrderCreateCostForm {

    @ApiModelProperty("运单费用ID")
    @NotNull(message = "运单费用id不能为空")
    private Long waybillCostId;

    @ApiModelProperty("费用")
    @NotNull(message = "本次应付费用不能为空")
    private BigDecimal payableAmount;

    private String remark;
}