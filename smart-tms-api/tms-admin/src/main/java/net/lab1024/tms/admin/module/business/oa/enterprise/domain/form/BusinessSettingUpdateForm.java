package net.lab1024.tms.admin.module.business.oa.enterprise.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 更新业务设置
 *
 * @author lidoudou
 * @date 2022/10/24 下午3:59
 */
@Data
public class BusinessSettingUpdateForm {

    @ApiModelProperty("企业ID")
    @NotNull(message = "企业ID不能为空")
    private Long enterpriseId;

    @ApiModelProperty("业务设置key")
    @NotBlank(message = "业务设置类型不能为空")
    @Length(max = 60000, message = "业务设置类型最多60000个字符")
    private String settingKey;

    @ApiModelProperty("业务设置内容")
    private String settingValue;
}
