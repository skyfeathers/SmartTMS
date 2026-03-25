package net.lab1024.tms.admin.module.business.oa.bank.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * OA银行信息新建
 *
 * @author lihaifan
 * @date 2022/6/23 11:50
 */
@Data
public class BankCreateForm {

    @ApiModelProperty("开户银行")
    @NotBlank(message = "开户银行不能为空")
    @Length(max = 200, message = "开户银行最多200字符")
    private String bankName;

    @ApiModelProperty("账户名称")
    @NotBlank(message = "账户名称不能为空")
    @Length(max = 200, message = "账户名称最多200字符")
    private String accountName;

    @ApiModelProperty("账号")
    @NotBlank(message = "账号不能为空")
    @Length(max = 200, message = "账号最多200字符")
    private String accountNumber;

    @ApiModelProperty("备注")
    @Length(max = 500, message = "备注最多500字符")
    private String remark;

    @ApiModelProperty("是否对公")
    @NotNull(message = "是否对公不能为空")
    private Boolean businessFlag;

    @ApiModelProperty("企业")
    @NotNull(message = "企业不能为空")
    private Long enterpriseId;

    @ApiModelProperty("禁用状态")
    @NotNull(message = "禁用状态不能为空")
    private Boolean disabledFlag;

    @ApiModelProperty(value = "创建人", hidden = true)
    private Long createUserId;
    @ApiModelProperty(value = "创建人", hidden = true)
    private String createUserName;
}
