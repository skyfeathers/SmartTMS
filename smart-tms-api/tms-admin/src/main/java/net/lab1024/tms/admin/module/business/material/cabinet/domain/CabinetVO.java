package net.lab1024.tms.admin.module.business.material.cabinet.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 业务资料-柜型管理
 *
 * @author lihaifan
 * @date 2022/6/24 11:32
 */
@Data
public class CabinetVO {

    @ApiModelProperty("柜型ID")
    private Long cabinetId;

    @ApiModelProperty("柜型名称")
    private String cabinetName;

    @ApiModelProperty("柜型皮重")
    private BigDecimal tare;

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
