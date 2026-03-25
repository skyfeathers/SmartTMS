package net.lab1024.tms.admin.module.business.pay.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.common.validator.enumeration.CheckEnum;
import net.lab1024.tms.common.module.business.pay.constant.PayOrderTypeEnum;
import net.lab1024.tms.common.module.business.waybill.constant.WaybillSettleTypeEnum;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author yandy
 * @description:
 * @date 2022/8/15 11:53 上午
 */
@Data
public class PayOrderCreateForm {

    @ApiModelPropertyEnum(value = PayOrderTypeEnum.class, desc = "付款单类型类型")
    @CheckEnum(value = PayOrderTypeEnum.class, required = true, message = "付款单类型错误")
    private Integer payOrderType;

    @ApiModelProperty("运单id")
    @NotNull(message = "运单id不能为空")
    private Long waybillId;

    @ApiModelProperty("现金支付银行卡id")
    private Long bankId;

    @ApiModelProperty("油卡支付油卡id")
    private Long oilCardId;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty(hidden = true)
    private Long enterpriseId;
}