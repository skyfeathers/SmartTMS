package net.lab1024.tms.admin.module.business.material.customsbroker.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 业务资料-报关行-编辑
 *
 * @author lihaifan
 * @date 2022/6/24 11:33
 */
@Data
public class CustomsBrokerUpdateForm extends CustomsBrokerCreateForm {

    @ApiModelProperty("报关行ID")
    @NotNull(message = "报关行ID不能为空")
    private Long customsBrokerId;
}
