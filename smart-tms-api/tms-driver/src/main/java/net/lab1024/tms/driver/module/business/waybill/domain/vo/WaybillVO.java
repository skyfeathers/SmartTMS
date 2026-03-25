package net.lab1024.tms.driver.module.business.waybill.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.module.business.flow.FlowAuditStatusEnum;
import net.lab1024.tms.common.module.business.order.constant.OrderTypeEnum;
import net.lab1024.tms.common.module.business.waybill.constant.WaybillSettleModeEnum;
import net.lab1024.tms.common.module.business.waybill.constant.WaybillSettleTypeEnum;
import net.lab1024.tms.common.module.business.waybill.constant.WaybillStatusEnum;
import net.lab1024.tms.common.module.business.waybill.constant.WaybillTransportModeEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author zhuoda
 * @Date 2022-07-14
 */
@Data
public class WaybillVO {
    
    @ApiModelProperty("运单id")
    private Long waybillId;
    
    @ApiModelProperty("运单号")
    private String waybillNumber;
    
    @ApiModelPropertyEnum(value = FlowAuditStatusEnum.class, desc = "运单审核状态")
    private Integer auditStatus;
    
    @ApiModelPropertyEnum(value = WaybillStatusEnum.class, desc = "运单状态")
    private Integer waybillStatus;
    
    @ApiModelPropertyEnum(value = OrderTypeEnum.class, desc = "是否为网络货运付款单")
    private Integer orderType;
    
    @ApiModelProperty("订单id")
    private Long orderId;
    
    @ApiModelProperty("订单号")
    private String orderNo;

    @ApiModelProperty("运输类型")
    private Integer businessTypeCode;
    
    @ApiModelProperty("装货时间")
    private LocalDateTime loadTime;

    @ApiModelProperty("卸货时间")
    private LocalDateTime unloadTime;
    
    @ApiModelProperty("货主id")
    private Long shipperId;
    
    @ApiModelProperty("货主名称")
    private String shipperName;
    
    @ApiModelProperty("货主简称")
    private String shortName;
    
    @ApiModelProperty("所属公司id")
    private Long enterpriseId;
    
    @ApiModelProperty("所属公司司名称")
    private String enterpriseName;
    
    @ApiModelProperty("箱号")
    private String containerNumber;
    
    @ApiModelProperty("铅封号")
    private String leadSealNumber;

    @ApiModelPropertyEnum(value = WaybillTransportModeEnum.class, desc = "运输方式")
    private Integer transportMode;
    
    @ApiModelPropertyEnum(value = WaybillSettleModeEnum.class, desc = "结算方式")
    private Integer settleMode;
    
    @ApiModelPropertyEnum(value = WaybillSettleTypeEnum.class, desc = "结算类型")
    private Integer settleType;
    
    @ApiModelProperty("结算类型描述")
    private String settleTypeDesc;

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

    @ApiModelProperty("发货日期")
    private LocalDateTime deliverGoodsTime;
    
    @ApiModelProperty("收货日期")
    private LocalDateTime receiveGoodsTime;
    
    @ApiModelProperty("提箱地点和相关联系人")
    private String containerLocation;
    
    @ApiModelProperty("装货地点和相关联系人")
    private String placingLocation;
    
    @ApiModelProperty("卸货地点和相关联系人")
    private String unloadingLocation;
    
    @ApiModelProperty("还箱地点和相关联系人")
    private String returnContainerLocation;
    
    @ApiModelProperty("备注")
    private String remark;
    
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

}
