package net.lab1024.tms.admin.module.business.material.businesstype.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 业务资料-业务类型-编辑
 *
 * @author lihaifan
 * @date 2022/6/24 11:33
 */
@Data
public class BusinessTypeUpdateForm extends BusinessTypeCreateForm {

    @ApiModelProperty("业务类型ID")
    @NotNull(message = "业务类型ID不能为空")
    private Long businessTypeId;

}
