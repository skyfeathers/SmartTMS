package net.lab1024.tms.admin.module.business.pay.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.module.business.material.cost.constants.CostItemCategoryEnum;
import net.lab1024.tms.common.module.business.material.cost.constants.CostItemTypeEnum;

import java.math.BigDecimal;

/**
 * @author yandy
 * @description:
 * @date 2022/8/15 11:53 上午
 */
@Data
public class PayOrderCostVO {

    @ApiModelProperty("付款单费用id")
    private Long payOrderCostId;

    @ApiModelProperty("付款单id")
    private Long payOrderId;

    @ApiModelProperty("运单id")
    private Long waybillId;

    @ApiModelProperty("运单费用ID")
    private Long waybillCostId;

    @ApiModelProperty("费用项id")
    private Long costItemId;

    @ApiModelPropertyEnum(value = CostItemTypeEnum.class, desc = "费用项类型")
    private Integer costItemType;

    @ApiModelPropertyEnum(value = CostItemCategoryEnum.class, desc = "费用分类")
    private Integer costItemCategory;

    @ApiModelProperty("费用项名称")
    private String costItemName;

    @ApiModelProperty("应付金额")
    private BigDecimal costAmount;

    @ApiModelProperty("备注")
    private String remark;
}
