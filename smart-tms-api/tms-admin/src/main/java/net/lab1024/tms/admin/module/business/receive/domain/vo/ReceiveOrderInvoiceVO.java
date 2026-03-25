package net.lab1024.tms.admin.module.business.receive.domain.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.deserializer.FileKeyVoDeserializer;
import net.lab1024.tms.common.common.serializer.FileKeyVoSerializer;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.module.business.receive.constant.InvoiceKindTypeEnum;
import net.lab1024.tms.common.module.business.receive.constant.InvoiceStatusEnum;
import net.lab1024.tms.common.module.business.receive.constant.InvoiceTypeEnum;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 应收帐款的受票方信息
 *
 * @author lidoudou
 * @date 2022/8/15 下午5:45
 */
@Data
public class ReceiveOrderInvoiceVO {

    @ApiModelPropertyEnum(value = InvoiceStatusEnum.class, desc = "开票状态")
    private Integer invoiceStatus;

    @ApiModelPropertyEnum(value = InvoiceTypeEnum.class, desc = "发票类型")
    private Integer invoiceType;

    @ApiModelPropertyEnum(value = InvoiceKindTypeEnum.class, desc = "发票种类")
    private Integer invoiceKindType;

    @ApiModelProperty("开票金额")
    private BigDecimal invoiceAmount;

    @ApiModelProperty("开票抬头")
    private String invoiceName;

    @ApiModelProperty("纳税识别号")
    private String invoiceNo;

    @ApiModelProperty("开票地址")
    private String invoiceAddress;

    @ApiModelProperty("开票银行")
    private String invoiceBankName;

    @ApiModelProperty("开户行号")
    private String invoiceBankNo;

    @ApiModelProperty("开票时间")
    private LocalDate invoiceTime;

    @ApiModelProperty("费用发票号")
    private String invoiceNumber;

    @ApiModelProperty("快递单号")
    private String courierNumber;

    @ApiModelProperty("开票附件")
    @JsonSerialize(using = FileKeyVoSerializer.class)
    @JsonDeserialize(using = FileKeyVoDeserializer.class)
    private String invoiceAttachment;

    @ApiModelProperty(value = "开票人", hidden = true)
    private Long invoiceUserId;

    @ApiModelProperty("开票人")
    private String invoiceUserName;
}
