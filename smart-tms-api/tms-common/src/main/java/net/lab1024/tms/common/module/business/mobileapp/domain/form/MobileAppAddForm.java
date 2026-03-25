package net.lab1024.tms.common.module.business.mobileapp.domain.form;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.deserializer.FileKeyVoDeserializer;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.common.validator.enumeration.CheckEnum;
import net.lab1024.tms.common.module.business.mobileapp.constant.MobileAppPlatformTypeEnum;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author yandy
 * @description:
 * @date 2023/5/16 11:16 下午
 */
@Data
public class MobileAppAddForm {

    @ApiModelPropertyEnum(value = MobileAppPlatformTypeEnum.class, desc = "平台类型")
    @CheckEnum(value = MobileAppPlatformTypeEnum.class, required = true, message = "平台类型错误")
    private Integer platformType;

    @ApiModelProperty("版本号")
    @NotBlank(message = "版本号错误")
    private String versionNo;

    @ApiModelProperty("版本更新描述")
    private String updateDesc;

    @ApiModelProperty("是否强制更新")
    @NotNull(message = "是否强制更新不能为空")
    private Boolean forceUpdateFlag;

    @ApiModelProperty("是否是最新版")
    @NotNull(message = "是否是最新版")
    private Boolean newestFlag;

    @ApiModelProperty("app文件key")
    @JsonDeserialize(using = FileKeyVoDeserializer.class)
    @NotBlank(message = "app文件不能为空")
    private String appFile;

}