package net.lab1024.tms.admin.module.business.maintenance.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 保养内容信息
 *
 * @author zhaoxinyang
 * @date 2023/11/14 14:27
 */
@Data
public class MaintenanceContentVO {

    @ApiModelProperty("保养内容")
    private String maintenanceContent;

    @ApiModelProperty("保养费用")
    private BigDecimal maintenanceAmount;

}