package net.lab1024.tms.common.module.business.contract.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 创建合同
 *
 * @author lihaifan
 * @date 2022/7/15 16:34
 */
@Data
public class ContractUpdateForm extends ContractCreateForm{


    @ApiModelProperty("合同Id")
    @NotNull(message = "合同id不能为空")
    private Integer contractId;

    @ApiModelProperty(hidden = true)
    private Long operatorId;
}
