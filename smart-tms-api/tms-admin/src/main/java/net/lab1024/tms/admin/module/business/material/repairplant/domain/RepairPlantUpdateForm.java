package net.lab1024.tms.admin.module.business.material.repairplant.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 维修厂 更新
 *
 * @author: Turbolisten
 * @date: 2022/7/13 21:50
 */
@Data
public class RepairPlantUpdateForm extends RepairPlantAddForm {

    @ApiModelProperty("维修厂id")
    @NotNull(message = "维修厂id不能为空")
    private Integer repairPlantId;
}
