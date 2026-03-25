package net.lab1024.tms.admin.module.business.material.cost.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.common.validator.enumeration.CheckEnum;
import net.lab1024.tms.common.module.business.material.cost.constants.CostItemCategoryEnum;
import net.lab1024.tms.common.module.business.material.cost.constants.CostItemTypeEnum;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author zhuoda
 * @Date 2022-07-18
 */
@Data
public class CostItemForm {

    @ApiModelProperty("名称")
    @NotBlank(message = "名称不能为空")
    private String name;

    @ApiModelPropertyEnum(CostItemTypeEnum.class)
    @CheckEnum(value = CostItemTypeEnum.class,required = true,message = "费用类型错误")
    private Integer type;

    @ApiModelPropertyEnum(CostItemCategoryEnum.class)
    @CheckEnum(value = CostItemCategoryEnum.class,required = true,message = "费用分类错误")
    private Integer category;

    @ApiModelProperty("排序")
    @NotNull(message = "排序不能为空")
    private Integer seq;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty(value = "创建人", hidden = true)
    private Long createUserId;

    @ApiModelProperty(value = "创建人", hidden = true)
    private String createUserName;

}
