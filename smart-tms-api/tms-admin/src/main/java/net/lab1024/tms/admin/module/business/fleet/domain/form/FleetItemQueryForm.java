package net.lab1024.tms.admin.module.business.fleet.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.domain.PageParam;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.module.business.fleet.constants.FleetItemTypeEnum;

import javax.validation.constraints.NotNull;

@Data
public class FleetItemQueryForm extends PageParam {

    @ApiModelProperty("关键字")
    private String keyWords;

    @ApiModelProperty("车队ID")
    @NotNull(message = "车队ID不能为空")
    private Long fleetId;

    @ApiModelPropertyEnum(value = FleetItemTypeEnum.class, desc = "类型", hidden = true)
    private Integer itemType;

    @ApiModelProperty(value = "删除标识", hidden = true)
    private Boolean deletedFlag;

}
