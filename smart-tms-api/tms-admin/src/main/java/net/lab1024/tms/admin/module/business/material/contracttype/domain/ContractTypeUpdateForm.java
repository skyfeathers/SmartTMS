package net.lab1024.tms.admin.module.business.material.contracttype.domain;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author zhuoda
 * @Date 2022-07-18
 */
@Data
public class ContractTypeUpdateForm extends ContractTypeForm {

    @NotNull(message = "合同类型id不能为空")
    private Long contractTypeId;

}
