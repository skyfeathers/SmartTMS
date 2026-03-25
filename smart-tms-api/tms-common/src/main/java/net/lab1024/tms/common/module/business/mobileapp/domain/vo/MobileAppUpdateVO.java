package net.lab1024.tms.common.module.business.mobileapp.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.module.business.mobileapp.constant.MobileAppUpdateTypeEnum;

import java.time.LocalDate;

/**
 * 用户版本查询结果VO类
 *
 * @author yandy
 * @date 2023/5/16 17:18
 */
@Data
public class MobileAppUpdateVO {

    @ApiModelPropertyEnum(value = MobileAppUpdateTypeEnum.class, desc = "更新类型")
    private Integer appUpdateType;

    @ApiModelProperty("文件大小")
    private Long newVersionFileSize;

    @ApiModelProperty("用户版本号")
    private String currentVersionNo;

    @ApiModelProperty("新版本号")
    private String newVersionNo;

    @ApiModelProperty("更新日期")
    private LocalDate updateDate;

    @ApiModelProperty("更新描述")
    private String updateDesc;

    @ApiModelProperty("更新URL")
    private String updateUrl;

}
