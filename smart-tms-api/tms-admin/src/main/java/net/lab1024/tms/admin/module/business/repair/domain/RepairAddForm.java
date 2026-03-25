package net.lab1024.tms.admin.module.business.repair.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.module.business.repair.RepairModuleTypeEnum;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.common.validator.enumeration.CheckEnum;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/***
 * 新增维修信息
 *
 * @author lidoudou
 * @date 2022/6/27 上午10:20
 */
@Data
public class RepairAddForm extends RepairBaseDTO {

    @ApiModelProperty("模块ID")
    @NotNull(message = "模块ID不能为空")
    private Long moduleId;

    @ApiModelPropertyEnum(desc = "模块类型", value = RepairModuleTypeEnum.class)
    @NotNull(message = "模块类型不能为空")
    @CheckEnum(value = RepairModuleTypeEnum.class, message = "模块类型错误")
    private Integer moduleType;

    @Valid
    @Size(min = 1, message = "维修内容和维修金额数据不能为空")
    @NotNull(message = "维修内容和维修金额不能为空")
    @ApiModelProperty("维修内容")
    private List<RepairContentForm> contentFormList;

    @ApiModelProperty(hidden = true)
    private Long enterpriseId;
}
