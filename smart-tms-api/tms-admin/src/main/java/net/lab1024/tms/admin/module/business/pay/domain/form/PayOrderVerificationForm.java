package net.lab1024.tms.admin.module.business.pay.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author yandy
 * @description:
 * @date 2022/8/15 5:29 下午
 */
@Data
public class PayOrderVerificationForm {

    @ApiModelProperty("付款单id")
    @NotNull(message = "付款单不能为空")
    private Long payOrderId;

    @ApiModelProperty("附件")
    private String verificationAttachment;

    @ApiModelProperty("备注")
    private String verificationRemark;

}
