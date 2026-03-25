package net.lab1024.tms.admin.module.business.receive.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 应收帐款作废
 *
 * @author lidoudou
 * @date 2022/8/6 上午10:33
 */
@Data
public class ReceiveOrderInvoiceCancelForm {

    @ApiModelProperty("申请开票ID")
    @NotNull(message = "申请开票ID不能为空")
    private Long receiveOrderInvoiceId;

    @ApiModelProperty("作废备注")
    private String remark;

    @ApiModelProperty(value = "操作人", hidden = true)
    private Long operateUserId;
}
