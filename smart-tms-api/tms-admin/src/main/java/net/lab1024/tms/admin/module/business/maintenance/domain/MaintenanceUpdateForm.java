package net.lab1024.tms.admin.module.business.maintenance.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 修改保养信息
 *
 * @author zhaoxinyang
 * @date 2023/10/18 14:08
 */
@Data
public class MaintenanceUpdateForm extends MaintenanceAddForm{

    @ApiModelProperty("保养表ID")
    @NotNull(message = "保养表ID不能为空")
    private Long maintenanceId;
    
}