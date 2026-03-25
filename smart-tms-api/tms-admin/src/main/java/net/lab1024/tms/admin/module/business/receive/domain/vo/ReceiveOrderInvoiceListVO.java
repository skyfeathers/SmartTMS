package net.lab1024.tms.admin.module.business.receive.domain.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.deserializer.FileKeyVoDeserializer;
import net.lab1024.tms.common.common.serializer.FileKeyVoSerializer;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.module.business.receive.constant.InvoiceKindTypeEnum;
import net.lab1024.tms.common.module.business.receive.constant.InvoiceTypeEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 申请开票列表VO
 *
 * @author lidoudou
 * @date 2022/8/17 下午4:13
 */
@Data
public class ReceiveOrderInvoiceListVO {

    @ApiModelProperty("收款单ID")
    private Long receiveOrderId;

    @ApiModelProperty("申请发票ID")
    private Long receiveOrderInvoiceId;

    @ApiModelProperty("收款单号")
    private String receiveOrderNumber;

    @ApiModelProperty("货主ID")
    private Long shipperId;

    @ApiModelProperty("货主名称")
    private String consignor;

    @ApiModelProperty("货主名称")
    private String shortName;

    @ApiModelProperty("订单所属公司")
    private Long enterpriseId;

    @ApiModelProperty("订单所属公司")
    private String enterpriseName;

    @ApiModelProperty("税点")
    private BigDecimal taxPoint;

    @ApiModelProperty("开票备注")
    private String invoiceRemark;

    @ApiModelProperty("费用发票号")
    private String invoiceNumber;

    @ApiModelProperty("创建人")
    private String createUserName;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelPropertyEnum(value = InvoiceTypeEnum.class, desc = "发票类型")
    private Integer invoiceType;

    @ApiModelPropertyEnum(value = InvoiceKindTypeEnum.class, desc = "发票种类")
    private Integer invoiceKindType;

    @ApiModelProperty("开票人")
    private Long invoiceUserId;

    @ApiModelProperty("开票人姓名")
    private String invoiceUserName;

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

    @ApiModelProperty("开票附件")
    @JsonSerialize(using = FileKeyVoSerializer.class)
    @JsonDeserialize(using = FileKeyVoDeserializer.class)
    private String invoiceAttachment;

    @ApiModelProperty("对账单附件")
    @JsonSerialize(using = FileKeyVoSerializer.class)
    @JsonDeserialize(using = FileKeyVoDeserializer.class)
    private String billAttachment;

    @ApiModelProperty("开票时间")
    private LocalDateTime invoiceTime;

    @ApiModelProperty("快递单号")
    private String courierNumber;
}
