package net.lab1024.tms.admin.module.business.waybill.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.module.business.material.cost.constants.CostItemCategoryEnum;
import net.lab1024.tms.common.module.business.material.cost.constants.CostItemTypeEnum;
import net.lab1024.tms.common.module.business.order.constant.OrderTypeEnum;
import net.lab1024.tms.common.module.business.waybill.constant.WaybillSettleTypeEnum;

import java.math.BigDecimal;

/**
 * @author yandy
 * @description:
 * @date 2022/8/15 11:53 上午
 */
@Data
public class WaybillSettleCostVO {

    @ApiModelProperty("运单费用ID")
    private Long waybillCostId;

    @ApiModelProperty("运单id")
    private Long waybillId;

    @ApiModelProperty("运单号")
    private String waybillNumber;

    @ApiModelProperty("货主id")
    private Long shipperId;

    @ApiModelProperty("货主名称")
    private String shipperName;

    @ApiModelProperty("所属公司id")
    private Long enterpriseId;

    @ApiModelProperty("所属公司司名称")
    private String enterpriseName;

    @ApiModelProperty("箱号")
    private String containerNumber;

    @ApiModelPropertyEnum(value = WaybillSettleTypeEnum.class, desc = "结算类型")
    private Integer settleType;

    @ApiModelProperty("司机")
    private Long driverId;

    @ApiModelProperty("司机名称")
    private String driverName;

    @ApiModelProperty("司机手机号")
    private String driverTelephone;

    @ApiModelProperty("车辆")
    private Long vehicleId;

    @ApiModelProperty("车牌号")
    private String vehicleNumber;

    @ApiModelProperty("车队")
    private Long fleetId;

    @ApiModelProperty("车队名称")
    private String fleetName;

    @ApiModelProperty("车队长电话")
    private String fleetCaptainPhone;

    @ApiModelProperty("费用项id")
    private Long costItemId;

    @ApiModelPropertyEnum(value = CostItemTypeEnum.class, desc = "费用项类型")
    private Integer costItemType;

    @ApiModelPropertyEnum(value = CostItemCategoryEnum.class, desc = "费用项分类")
    private Integer costItemCategory;

    @ApiModelProperty("费用项名称")
    private String costItemName;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("总费用")
    private BigDecimal costAmount;

    @ApiModelProperty("本次应支付金额")
    private BigDecimal thisPayableAmount;
}