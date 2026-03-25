package net.lab1024.tms.admin.module.business.repair.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 维修内容信息
 *
 * @author zhaoxinyang
 * @date 2023/10/17 14:27
 */
@Data
public class RepairContentVO {

    @ApiModelProperty("维修内容")
    private String repairContent;

    @ApiModelProperty("维修费用")
    private BigDecimal repairAmount;

}