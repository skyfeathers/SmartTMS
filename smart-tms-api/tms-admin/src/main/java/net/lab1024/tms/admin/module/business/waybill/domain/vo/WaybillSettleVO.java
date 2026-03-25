package net.lab1024.tms.admin.module.business.waybill.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.module.business.order.constant.OrderTypeEnum;
import net.lab1024.tms.common.module.business.waybill.constant.WaybillSettleTypeEnum;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author yandy
 * @description:
 * @date 2022/8/15 11:53 上午
 */
@Data
public class WaybillSettleVO {

    @ApiModelPropertyEnum(value = WaybillSettleTypeEnum.class, desc = "结算类型")
    private Integer settleType;

    @ApiModelProperty("所属公司id")
    private Long enterpriseId;

    @ApiModelProperty("结算对象id")
    private Long settleObjectId;

    @ApiModelProperty("结算对象名称")
    private String settleObjectName;

    @ApiModelProperty("结算对象电话")
    private String settleObjectPhone;

    @ApiModelProperty("结算费用信息")
    private List<WaybillSettleCostVO> settleCostList;


}