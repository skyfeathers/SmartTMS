package net.lab1024.tms.admin.module.business.contract.basic.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.common.validator.enumeration.CheckEnum;
import net.lab1024.tms.common.module.business.contract.constant.ContractStatusEnum;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 合同批量修改状态
 *
 * @author lihaifan
 * @date 2022/7/15 18:44
 */
@Data
public class ContractUpdateStatusForm {

    @ApiModelProperty("合同ID")
    @NotNull(message = "请选择合同")
    private List<Long> contractIdList;

    @ApiModelPropertyEnum(value = ContractStatusEnum.class, desc = "合同状态")
    @CheckEnum(value = ContractStatusEnum.class, required = true, message = "合同状态错误")
    private Integer status;

    @ApiModelProperty(hidden = true)
    private Long operatorId;
}
