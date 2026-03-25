package net.lab1024.tms.admin.module.business.oa.invoice.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * OA发票信息编辑
 *
 * @author lihaifan
 * @date 2022/6/23 17:32
 */
@Data
public class InvoiceUpdateForm extends InvoiceCreateForm{

    @ApiModelProperty("发票信息ID")
    @NotNull(message = "发票信息ID不能为空")
    private Long invoiceId;
}
