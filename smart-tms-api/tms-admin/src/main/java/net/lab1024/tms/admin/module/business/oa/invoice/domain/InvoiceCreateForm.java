package net.lab1024.tms.admin.module.business.oa.invoice.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * OA发票信息新建
 *
 * @author lihaifan
 * @date 2022/6/23 17:32
 */
@Data
public class InvoiceCreateForm {

    @ApiModelProperty("开票抬头")
    @NotBlank(message = "开票抬头不能为空")
    @Length(max = 200, message = "开票抬头最多200字符")
    private String invoiceHeads;

    @ApiModelProperty("纳税人识别号")
    @NotBlank(message = "纳税人识别号不能为空")
    @Length(max = 200, message = "纳税人识别号最多200字符")
    private String taxpayerIdentificationNumber;

    @ApiModelProperty("银行账户")
    @NotBlank(message = "银行账户不能为空")
    @Length(max = 200, message = "银行账户最多200字符")
    private String accountNumber;

    @ApiModelProperty("开户行")
    @NotBlank(message = "开户行不能为空")
    @Length(max = 200, message = "开户行最多200字符")
    private String bankName;

    @ApiModelProperty("启用状态")
    @NotNull(message = "启用状态不能为空")
    private Boolean disabledFlag;

    @ApiModelProperty("备注")
    @Length(max = 500, message = "备注最多500字符")
    private String remark;

    @ApiModelProperty("企业")
    @NotNull(message = "企业不能为空")
    private Long enterpriseId;

    @ApiModelProperty(value = "创建人", hidden = true)
    private Long createUserId;

    @ApiModelProperty(value = "创建人", hidden = true)
    private String createUserName;
}
