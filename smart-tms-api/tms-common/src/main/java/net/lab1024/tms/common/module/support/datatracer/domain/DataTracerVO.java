package net.lab1024.tms.common.module.support.datatracer.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.module.support.datatracer.constant.DataTracerBusinessTypeEnum;

import java.time.LocalDateTime;

/**
 * [  ]
 *
 * @author 罗伊
 */
@Data
public class DataTracerVO {

    @ApiModelProperty("日志id")
    private Long dataTracerId;

    @ApiModelProperty("单据id")
    private Long businessId;

    @ApiModelPropertyEnum(value = DataTracerBusinessTypeEnum.class, desc = "业务类型")
    private Integer businessType;

    @ApiModelProperty("单据类型描述")
    private String businessTypeDesc;

    @ApiModelProperty("操作类型")
    private Integer operateType;

    @ApiModelProperty("操作类型描述")
    private String operateTypeDesc;

    @ApiModelProperty("操作内容")
    private String operateContent;

    @ApiModelProperty("不同")
    private String diff;

    @ApiModelProperty("操作人")
    private Long operatorId;

    @ApiModelProperty("操作人名称")
    private String operatorName;

    @ApiModelProperty("userAgent")
    private String userAgent;

    @ApiModelProperty("ip")
    private String ip;

    @ApiModelProperty("os")
    private String os;

    @ApiModelProperty("browser")
    private String browser;

    @ApiModelProperty("platform")
    private String platform;

    @ApiModelProperty("操作时间")
    private LocalDateTime createTime;

}
