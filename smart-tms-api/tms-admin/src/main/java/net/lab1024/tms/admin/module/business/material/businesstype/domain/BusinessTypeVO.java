package net.lab1024.tms.admin.module.business.material.businesstype.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 业务资料-业务类型
 *
 * @author lihaifan
 * @date 2022/6/24 11:32
 */
@Data
public class BusinessTypeVO {

    @ApiModelProperty("业务类型ID")
    private Long businessTypeId;

    @ApiModelProperty("业务类型名称")
    private String businessTypeName;

    @ApiModelProperty("业务代码")
    private String businessTypeCode;

    @ApiModelProperty("运输类型")
    private Integer tripType;

    @ApiModelProperty("运输类型描述")
    private String tripTypeDesc;

    @ApiModelProperty("默认状态")
    private Boolean defaultFlag;

    @ApiModelProperty("禁用状态")
    private Boolean disabledFlag;

    @ApiModelProperty("创建人ID")
    private Long createUserId;

    @ApiModelProperty("创建人名称")
    private String createUserName;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;
}
