package net.lab1024.tms.admin.module.business.waybill.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.module.business.pay.constant.PayOrderStatusEnum;
import net.lab1024.tms.common.module.business.waybill.constant.WaybillSettleModeEnum;
import net.lab1024.tms.common.module.business.waybill.constant.WaybillSettleTypeEnum;
import net.lab1024.tms.common.module.business.waybill.constant.WaybillTransportModeEnum;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author yandy
 * @description:
 * @date 2022/9/29 4:13 下午
 */
@Data
public class WaybillProfitVO {

    @ApiModelProperty("运单id")
    private Long waybillId;

    @ApiModelProperty("运单号")
    private String waybillNumber;

    @ApiModelProperty("订单id")
    private Long orderId;

    @ApiModelProperty("订单号")
    private String orderNo;

    @ApiModelProperty("订单备注")
    private String orderRemark;

    @ApiModelProperty("业务类型")
    private String containerBusinessTypeName;

    @ApiModelProperty("应收单号")
    private String receiveOrderNumber;

    @ApiModelProperty("货主简称")
    private String shortName;

    @ApiModelProperty("所属公司司名称")
    private String enterpriseName;

    @ApiModelProperty("箱号")
    private String containerNumber;

    @ApiModelProperty("铅封号")
    private String leadSealNumber;

    @ApiModelProperty("柜型名称")
    private String cabinetName;

    @ApiModelPropertyEnum(value = WaybillSettleModeEnum.class, desc = "结算方式")
    private Integer settleMode;

    @ApiModelPropertyEnum(value = WaybillSettleTypeEnum.class, desc = "结算类型")
    private Integer settleType;

    @ApiModelProperty("司机名称")
    private String driverName;

    @ApiModelProperty("车牌号")
    private String vehicleNumber;

    @ApiModelProperty("本单利润")
    private BigDecimal profitAmount;

    @ApiModelProperty("本单税金")
    private BigDecimal taxAmount;

    @ApiModelProperty("应收总金额")
    private BigDecimal receiveAmount;

    @ApiModelProperty("应付总金额")
    private BigDecimal payableAmount;

    @ApiModelProperty("司机工资")
    private BigDecimal salaryAmount;

    @ApiModelProperty("在途费用")
    private BigDecimal carCostAmount;

    @ApiModelProperty("发货日期")
    private LocalDateTime deliverGoodsTime;

    @ApiModelProperty("收货日期")
    private LocalDateTime receiveGoodsTime;

    @ApiModelProperty("装卸货时间")
    private LocalDateTime loadTime;

    @ApiModelProperty("提箱地点和相关联系人")
    private String containerLocation;

    @ApiModelProperty("装货地点和相关联系人")
    private String placingLocation;

    @ApiModelProperty("卸货地点和相关联系人")
    private String unloadingLocation;

    @ApiModelProperty("还箱地点和相关联系人")
    private String returnContainerLocation;

    @ApiModelProperty("合同编号")
    private String contractCode;

    @ApiModelProperty("创建人名称")
    private String createUserName;

    @ApiModelProperty("货主业务负责人名称")
    private String shipperManagerName;

    @ApiModelProperty("客服-订单-创建人名称")
    private String orderCreateUserName;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("订单调度")
    private String scheduleName;


    @ApiModelProperty("业务时间")
    private LocalDate businessDate;

    @ApiModelProperty("应付费用信息")
    private List<WaybillProfitCostVO> costList;

    @ApiModelProperty("应收费用信息")
    private List<WaybillProfitReceiveCostVO> receiveCostList;

    @ApiModelPropertyEnum(value = PayOrderStatusEnum.class, desc = "支付状态")
    private Integer payStatus;

    @ApiModelPropertyEnum(value = WaybillTransportModeEnum.class, desc = "运输方式")
    private Integer transportMode;
}