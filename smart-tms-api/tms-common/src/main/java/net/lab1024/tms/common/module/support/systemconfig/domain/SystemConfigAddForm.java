package net.lab1024.tms.common.module.support.systemconfig.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

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
public class SystemConfigAddForm {

    @ApiModelProperty("参数key")
    @NotBlank(message = "参数key不能为空")
    @Length(max = 255, message = "参数key最多255个字符")
    private String configKey;

    @ApiModelProperty("参数的值")
    @NotBlank(message = "参数的值不能为空")
    @Length(max = 60000, message = "参数的值最多60000个字符")
    private String configValue;

    @ApiModelProperty("参数名称")
    @NotBlank(message = "参数名称不能为空")
    @Length(max = 255, message = "参数名称最多255个字符")
    private String configName;

    @ApiModelProperty("备注")
    @Length(max = 255, message = "备注最多255个字符")
    private String remark;
}
