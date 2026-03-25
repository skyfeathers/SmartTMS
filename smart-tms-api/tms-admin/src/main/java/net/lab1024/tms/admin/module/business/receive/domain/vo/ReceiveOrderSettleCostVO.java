package net.lab1024.tms.admin.module.business.receive.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.module.business.material.cost.constants.CostItemTypeEnum;

import java.math.BigDecimal;

/**
 * @author yandy
 * @description:
 * @date 2022/8/15 11:53 上午
 */
@Data
public class ReceiveOrderSettleCostVO {

    @ApiModelProperty("运单费用ID")
    private Long waybillReceiveCostId;

    @ApiModelProperty("运单id")
    private Long waybillId;

    @ApiModelProperty("运单号")
    private String waybillNumber;

    @ApiModelProperty("费用项id")
    private Long costItemId;

    @ApiModelPropertyEnum(value = CostItemTypeEnum.class, desc = "费用项类型")
    private Integer costItemType;

    @ApiModelProperty("费用项名称")
    private String costItemName;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("总费用")
    private BigDecimal costAmount;

    @ApiModelProperty("已收费用")
    private BigDecimal receivedCostAmount;

    @ApiModelProperty("待收费用")
    private BigDecimal waitReceivedCostAmount;

    @ApiModelProperty("本次应收金额")
    private BigDecimal thisPayableAmount;
}