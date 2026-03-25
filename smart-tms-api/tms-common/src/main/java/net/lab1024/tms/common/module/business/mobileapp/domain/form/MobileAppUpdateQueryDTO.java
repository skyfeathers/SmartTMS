package net.lab1024.tms.common.module.business.mobileapp.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.common.validator.enumeration.CheckEnum;
import net.lab1024.tms.common.module.business.mobileapp.constant.MobileAppPlatformTypeEnum;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * 客户端app版本查询
 *
 * @author yandy
 * @date 2023/5/16 17:18
 */
@Data
public class MobileAppUpdateQueryDTO {

    @CheckEnum(value = MobileAppPlatformTypeEnum.class, required = true, message = "平台类型错误")
    @ApiModelPropertyEnum(value = MobileAppPlatformTypeEnum.class, desc = "平台类型")
    private Integer platformType;

    @NotBlank(message = "客户端版本号不能为空")
    @Length(max = 20, message = "版本号长度不能超过20个字符")
    @ApiModelProperty(value = "客户端版本号", required = true)
    private String versionNo;

}
