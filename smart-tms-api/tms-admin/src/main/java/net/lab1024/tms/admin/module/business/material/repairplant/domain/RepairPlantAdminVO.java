package net.lab1024.tms.admin.module.business.material.repairplant.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.module.business.material.repairplant.domain.RepairPlantBaseDTO;

import java.time.LocalDateTime;

/**
 * 维修厂 更新
 *
 * @author: Turbolisten
 * @date: 2022/7/13 21:50
 */
@Data
public class RepairPlantAdminVO extends RepairPlantBaseDTO {

    @ApiModelProperty("维修厂id")
    private Integer repairPlantId;

    private String updateUserName;

    private String createUserName;

    private LocalDateTime updateTime;

    private LocalDateTime createTime;
}
