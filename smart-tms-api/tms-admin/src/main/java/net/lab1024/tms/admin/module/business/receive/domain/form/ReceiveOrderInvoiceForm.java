package net.lab1024.tms.admin.module.business.receive.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.module.business.receive.constant.InvoiceKindTypeEnum;
import net.lab1024.tms.common.module.business.receive.constant.InvoiceTypeEnum;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 提交受票方信息
 *
 * @author lidoudou
 * @date 2022/8/15 下午5:12
 */
@Data
public class ReceiveOrderInvoiceForm {

    @ApiModelPropertyEnum(value = InvoiceTypeEnum.class, desc = "发票类型")
    @NotNull(message = "发票类型不能为空")
    private Integer invoiceType;

    @ApiModelPropertyEnum(value = InvoiceKindTypeEnum.class, desc = "发票种类")
    @NotNull(message = "发票种类不能为空")
    private Integer invoiceKindType;

    @ApiModelProperty("开票金额")
    @NotNull(message = "开票金额不能为空")
    private BigDecimal invoiceAmount;

    @NotBlank(message = "纳税识别号")
    private String invoiceNo;

    @NotBlank(message = "开票抬头")
    private String invoiceName;

    @ApiModelProperty("开票电话")
    private String invoicePhone;

    @ApiModelProperty("开票银行")
    private String invoiceBankName;

    @ApiModelProperty("开户行号")
    private String invoiceBankNo;

    @ApiModelProperty("开票地址")
    private String invoiceAddress;
}
