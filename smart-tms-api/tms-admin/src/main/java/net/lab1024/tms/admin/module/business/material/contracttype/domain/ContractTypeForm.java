package net.lab1024.tms.admin.module.business.material.contracttype.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author zhuoda
 * @Date 2022-07-18
 */
@Data
public class ContractTypeForm {

    @ApiModelProperty("名称")
    @NotBlank(message = "名称不能为空")
    private String name;

    @ApiModelProperty("排序")
    @NotNull(message = "排序不能为空")
    private Integer seq;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty(value = "创建人", hidden = true)
    private Long createUserId;

    @ApiModelProperty(value = "创建人", hidden = true)
    private String createUserName;

}
