package net.lab1024.tms.admin.module.business.shipper.domain.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.deserializer.FileKeyVoDeserializer;
import net.lab1024.tms.common.common.serializer.FileKeyVoSerializer;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * [ 货主开票信息 ]
 *
 * @author yandanyang
 * @date 2020/7/30 9:27
 */
@Data
public class ShipperInvoiceDTO {

    @ApiModelProperty(value = "invoiceId")
    private Long invoiceId;

    @ApiModelProperty(value = "货主id", hidden = true)
    private Long shipperId;

    @ApiModelProperty("纳税识别号")
    @Length(max = 50, message = "纳税识别号不能超过50字符")
    private String invoiceNo;

    @ApiModelProperty("开票地址")
    @Length(max = 500, message = "开票地址不能超过500字符")
    private String invoiceAddress;

    @ApiModelProperty("银行账号")
    @Length(max = 50, message = "银行账号不能超过50字符")
    private String invoiceBankAccount;

    @ApiModelProperty("开票银行")
    @Length(max = 100, message = "开票行不能超过100字符")
    private String invoiceBankName;

    @ApiModelProperty("开票抬头")
    @Length(max = 100, message = "开票抬头不能超过100字符")
    @NotBlank(message = "开票抬头不能为空")
    private String invoiceName;

    @ApiModelProperty("开票电话")
    @Length(max = 50, message = "开票电话不能超过50字符")
    private String invoicePhone;

    @ApiModelProperty("开户行号")
    private String invoiceBankNo;

    @ApiModelProperty("开户行地址")
    private String invoiceBankAddress;

    @ApiModelProperty("中征码")
    private String loanCardNo;

    @ApiModelProperty("附件")
    @JsonSerialize(using = FileKeyVoSerializer.class)
    @JsonDeserialize(using = FileKeyVoDeserializer.class)
    private String attachment;

    @ApiModelProperty("禁用状态")
    private Boolean disableFlag;
}
