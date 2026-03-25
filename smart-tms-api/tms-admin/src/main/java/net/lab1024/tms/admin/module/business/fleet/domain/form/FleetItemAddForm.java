package net.lab1024.tms.admin.module.business.fleet.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.common.validator.enumeration.CheckEnum;
import net.lab1024.tms.common.module.business.fleet.constants.FleetItemTypeEnum;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 添加关联对象
 *
 * @author lidoudou
 * @date 2022/6/28 下午2:54
 */
@Data
public class FleetItemAddForm {

    @ApiModelProperty("车队ID")
    @NotNull(message = "车队ID不能为空")
    private Long fleetId;

    @ApiModelPropertyEnum(value = FleetItemTypeEnum.class, desc = "类型")
    @NotNull(message = "类型不能为空")
    @CheckEnum(value = FleetItemTypeEnum.class, message = "类型错误", required = true)
    private Integer itemType;

    @ApiModelProperty("关联ID")
    @NotNull(message = "关联ID不能为空")
    @Size(min = 1, message = "关联ID不能为空")
    private List<Long> itemIdList;
}
