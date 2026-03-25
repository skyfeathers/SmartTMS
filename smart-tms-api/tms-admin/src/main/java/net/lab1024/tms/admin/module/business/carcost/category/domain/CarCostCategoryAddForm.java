package net.lab1024.tms.admin.module.business.carcost.category.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.module.business.carcost.constant.CarCostCategoryCostTypeEnum;
import net.lab1024.tms.common.module.business.carcost.constant.CarCostCategoryPayModeEnum;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.common.validator.enumeration.CheckEnum;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 新建费用分类 Form
 *
 * @author zhaoxinyang
 * @date 2023/10/25 09:13
 */
@Data
public class CarCostCategoryAddForm {

    @NotNull(message = "费用类型不能为空")
    @CheckEnum(value = CarCostCategoryCostTypeEnum.class, message = "费用类型错误")
    @ApiModelPropertyEnum(value = CarCostCategoryCostTypeEnum.class, desc = "费用类型")
    private Integer costType;

    @ApiModelProperty("分类名称")
    @NotBlank(message = "分类名称不能为空")
    @Length(max = 50, message = "分类名称最多50个字符")
    private String categoryName;

    @NotNull(message = "支付方式不能为空")
    @CheckEnum(value = CarCostCategoryPayModeEnum.class, message = "支付方式错误")
    @ApiModelPropertyEnum(value = CarCostCategoryPayModeEnum.class, desc = "支付方式")
    private Integer payMode;

    @ApiModelProperty("排序")
    @NotNull(message = "排序不能为空")
    private Integer sort;

}