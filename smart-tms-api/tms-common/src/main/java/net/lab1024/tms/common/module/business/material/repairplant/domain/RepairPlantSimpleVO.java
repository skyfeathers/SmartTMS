package net.lab1024.tms.common.module.business.material.repairplant.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 维修厂家 选择 VO
 *
 * @author: Turbolisten
 * @date: 2022/7/14 15:00
 */
@Data
public class RepairPlantSimpleVO {

    @ApiModelProperty("维修厂家id")
    private Integer repairPlantId;

    @ApiModelProperty("维修厂家名称")
    private String repairPlantName;
}
