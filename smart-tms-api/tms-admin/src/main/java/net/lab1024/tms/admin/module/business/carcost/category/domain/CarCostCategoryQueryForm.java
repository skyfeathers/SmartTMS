package net.lab1024.tms.admin.module.business.carcost.category.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.module.business.carcost.constant.CarCostCategoryCostTypeEnum;
import net.lab1024.tms.common.module.business.carcost.constant.CarCostCategoryPayModeEnum;
import net.lab1024.tms.common.common.domain.PageParam;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.common.validator.enumeration.CheckEnum;
import org.hibernate.validator.constraints.Length;

/**
 * 费用分类 查询Form
 *
 * @author zhaoxinyang
 * @date 2023/10/25 08:48
 */
@Data
public class CarCostCategoryQueryForm extends PageParam {

    @ApiModelProperty(value = "关键字")
    @Length(max = 200, message = "关键最多200个字符")
    private String keywords;

    @CheckEnum(value = CarCostCategoryCostTypeEnum.class, message = "费用类型错误")
    @ApiModelPropertyEnum(value = CarCostCategoryCostTypeEnum.class, desc = "费用类型")
    private Integer costType;

    @CheckEnum(value = CarCostCategoryPayModeEnum.class, message = "支付方式错误")
    @ApiModelPropertyEnum(value = CarCostCategoryPayModeEnum.class, desc = "支付方式错误")
    private Integer payMode;

    @ApiModelProperty(value = "删除状态", hidden = true)
    private Boolean deletedFlag;
}