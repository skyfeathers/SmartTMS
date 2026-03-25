package net.lab1024.tms.common.module.business.mobileapp.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.domain.PageParam;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.module.business.mobileapp.constant.MobileAppPlatformTypeEnum;

/**
 * @author yandy
 * @description:
 * @date 2023/5/16 11:18 下午
 */
@Data
public class MobileAppQueryForm extends PageParam {

    @ApiModelPropertyEnum(value = MobileAppPlatformTypeEnum.class, desc = "平台类型")
    private String platformType;

    @ApiModelProperty(value = "关键字")
    private String keywords;

}