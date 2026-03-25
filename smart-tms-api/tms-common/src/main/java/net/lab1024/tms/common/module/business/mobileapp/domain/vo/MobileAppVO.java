package net.lab1024.tms.common.module.business.mobileapp.domain.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.serializer.FileKeyVoSerializer;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.module.business.mobileapp.constant.MobileAppPlatformTypeEnum;

import java.time.LocalDateTime;

/**
 * @author yandy
 * @description:
 * @date 2023/5/16 11:14 下午
 */
@Data
public class MobileAppVO {

    private Long id;

    @ApiModelPropertyEnum(value = MobileAppPlatformTypeEnum.class, desc = "平台类型")
    private Integer platformType;

    @ApiModelProperty("版本号")
    private String versionNo;

    @ApiModelProperty("版本更新描述")
    private String updateDesc;

    @ApiModelProperty("是否强制更新")
    private Boolean forceUpdateFlag;

    @ApiModelProperty("是否是最新版")
    private Boolean newestFlag;

    @ApiModelProperty("app文件key")
    @JsonSerialize(using = FileKeyVoSerializer.class)
    private String appFile;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

}