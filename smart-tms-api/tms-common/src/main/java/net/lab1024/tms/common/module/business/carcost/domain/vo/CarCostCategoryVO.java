package net.lab1024.tms.common.module.business.carcost.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.module.business.carcost.constant.CarCostCategoryCostTypeEnum;
import net.lab1024.tms.common.module.business.carcost.constant.CarCostCategoryPayModeEnum;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;

/**
 * 费用分类 VO
 *
 * @author zhaoxinyang
 * @date 2023/10/25 08:36
 */
@Data
public class CarCostCategoryVO {

    @ApiModelProperty("分类id")
    private Long categoryId;

    @ApiModelPropertyEnum(value = CarCostCategoryCostTypeEnum.class, desc = "费用项类型")
    private Integer costType;

    @ApiModelProperty("分类名称")
    private String categoryName;

    @ApiModelPropertyEnum(value = CarCostCategoryPayModeEnum.class, desc = "支付方式")
    private Integer payMode;

    @ApiModelProperty("排序")
    private Integer sort;

}