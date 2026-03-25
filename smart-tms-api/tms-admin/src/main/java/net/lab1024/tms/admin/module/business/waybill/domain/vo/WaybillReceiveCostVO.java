package net.lab1024.tms.admin.module.business.waybill.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.module.business.material.cost.constants.CostItemCategoryEnum;
import net.lab1024.tms.common.module.business.material.cost.constants.CostItemTypeEnum;

import java.math.BigDecimal;

/**
 * @author zhuoda
 * @Date 2022-07-14
 */
@Data
public class WaybillReceiveCostVO {

    @ApiModelProperty("运单费用ID")
    private Long waybillReceiveCostId;

    @ApiModelProperty("订单费用项ID")
    private Long orderCostId;

    @ApiModelProperty("运单id")
    private Long waybillId;

    @ApiModelProperty("费用项id")
    private Long costItemId;

    @ApiModelPropertyEnum(value = CostItemTypeEnum.class, desc = "费用项类型")
    private Integer costItemType;

    @ApiModelPropertyEnum(value = CostItemCategoryEnum.class, desc = "费用分类")
    private Integer costItemCategory;

    @ApiModelProperty("费用项名称")
    private String costItemName;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("费用")
    private BigDecimal costAmount;



}
