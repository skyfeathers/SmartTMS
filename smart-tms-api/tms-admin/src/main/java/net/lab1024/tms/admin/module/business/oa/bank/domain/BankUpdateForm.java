package net.lab1024.tms.admin.module.business.oa.bank.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * OA银行信息更新
 *
 * @author lihaifan
 * @date 2022/6/23 11:50
 */
@Data
public class BankUpdateForm extends BankCreateForm {

    @ApiModelProperty("银行信息ID")
    @NotNull(message = "银行信息ID不能为空")
    private Long bankId;
}
