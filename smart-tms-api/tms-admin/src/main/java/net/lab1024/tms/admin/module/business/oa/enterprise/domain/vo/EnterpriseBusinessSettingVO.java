package net.lab1024.tms.admin.module.business.oa.enterprise.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 公司业务设置
 *
 * @author lidoudou
 * @date 2022/10/24 下午2:31
 */
@Data
public class EnterpriseBusinessSettingVO {

    @ApiModelProperty("业务设置名称")
    private String settingName;

    @ApiModelProperty("业务设置key")
    private String settingKey;

    @ApiModelProperty("业务设置内容")
    private String settingValue;
}
