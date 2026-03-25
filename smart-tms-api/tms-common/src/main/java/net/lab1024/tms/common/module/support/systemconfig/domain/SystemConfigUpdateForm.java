package net.lab1024.tms.common.module.support.systemconfig.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * [  ]
 *
 * @author 罗伊
 * @version 1.0
 *
 * @date
 * @since JDK1.8
 */
@Data
public class SystemConfigUpdateForm extends SystemConfigAddForm {

    @ApiModelProperty("systemConfigId")
    @NotNull(message = "systemConfigId不能为空")
    private Long systemConfigId;
}
