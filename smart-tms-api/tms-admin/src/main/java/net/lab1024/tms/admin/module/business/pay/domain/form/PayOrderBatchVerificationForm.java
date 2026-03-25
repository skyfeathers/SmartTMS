package net.lab1024.tms.admin.module.business.pay.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author yandy
 * @description:
 * @date 2022/8/15 5:29 下午
 */
@Data
public class PayOrderBatchVerificationForm {

    @ApiModelProperty("付款单id")
    @NotEmpty(message = "付款单id 不能为空")
    private List<Long> payOrderIdList;

    @ApiModelProperty("附件")
    private String verificationAttachment;

    @ApiModelProperty("备注")
    private String verificationRemark;

}
