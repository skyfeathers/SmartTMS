package net.lab1024.tms.admin.module.business.receive.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * 提交订单对账
 *
 * @author lidoudou
 * @date 2022/7/20 下午5:41
 */
@Data
public class ReceiveOrderForm extends ReceiveOrderSubmitForm{

    @ApiModelProperty("税点")
    @NotNull(message = "税点不能为空")
    private BigDecimal taxPoint;


    @ApiModelProperty("邮寄信息")
    @NotNull(message = "邮寄信息不能为空")
    @Valid
    private ReceiveOrderMailForm receiveOrderMailForm;

    @ApiModelProperty("发票类型")
    @NotNull(message = "发票类型不能为空")
    @Valid
    private ReceiveOrderInvoiceForm receiveOrderInvoiceForm;


}
