package net.lab1024.tms.admin.module.business.oilcard.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author lidoudou
 * @description:
 * @date 2023/12/5 10:15
 */
@Data
public class OilCardPreRechargeAmountUpdateForm {

    @ApiModelProperty("油卡ID")
    @NotNull(message = "油卡ID不能为空")
    private Long oilCardId;

    @ApiModelProperty("计划充值金额")
    @NotNull(message = "计划充值金额不能为空")
    private BigDecimal preRechargeAmount;
}