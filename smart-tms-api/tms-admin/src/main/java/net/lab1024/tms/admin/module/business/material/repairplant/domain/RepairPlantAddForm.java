package net.lab1024.tms.admin.module.business.material.repairplant.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.module.business.material.repairplant.domain.RepairPlantBaseDTO;

/**
 * 维修厂 添加
 *
 * @author: Turbolisten
 * @date: 2022/7/13 21:50
 */
@Data
public class RepairPlantAddForm extends RepairPlantBaseDTO {

    @ApiModelProperty(hidden = true)
    private String updateUserName;

    @ApiModelProperty(hidden = true)
    private String createUserName;

    @ApiModelProperty(hidden = true)
    private Long enterpriseId;
}
