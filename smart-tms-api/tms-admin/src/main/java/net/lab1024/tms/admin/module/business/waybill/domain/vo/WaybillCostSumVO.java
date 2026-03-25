package net.lab1024.tms.admin.module.business.waybill.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.module.business.material.cost.constants.CostItemCategoryEnum;
import net.lab1024.tms.common.module.business.material.cost.constants.CostItemTypeEnum;

import java.math.BigDecimal;

/**
 * @author lidoudou
 * @date 2022/9/25 下午3:44
 */
@Data
public class WaybillCostSumVO {

    @ApiModelProperty("运单ID")
    private Long waybillId;

    @ApiModelPropertyEnum(value = CostItemCategoryEnum.class, desc = "费用分类")
    private Integer costItemCategory;

    @ApiModelPropertyEnum(value = CostItemTypeEnum.class, desc = "费用项类型")
    private Integer costItemType;

    @ApiModelProperty("合计")
    private BigDecimal costAmount;
}
