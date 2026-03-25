package net.lab1024.tms.admin.module.business.waybill.domain.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.deserializer.FileKeyVoDeserializer;
import net.lab1024.tms.common.common.serializer.FileKeyVoSerializer;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.module.business.flow.FlowAuditStatusEnum;
import net.lab1024.tms.common.module.business.pay.constant.PayOrderStatusEnum;
import net.lab1024.tms.common.module.business.waybill.constant.WaybillSettleModeEnum;
import net.lab1024.tms.common.module.business.waybill.constant.WaybillSettleTypeEnum;
import net.lab1024.tms.common.module.business.waybill.constant.WaybillStatusEnum;
import net.lab1024.tms.common.module.business.waybill.constant.WaybillTransportModeEnum;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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

    @ApiModelProperty("订单id")
    private Long orderId;

    @ApiModelProperty("订单号")
    private String orderNo;

    @ApiModelProperty("客户订单号")
    private String shipperOrderNumber;

    @ApiModelProperty("装货时间")
    private LocalDateTime loadTime;

    @ApiModelProperty("卸货时间")
    private LocalDateTime unloadTime;

    @ApiModelProperty("订单备注")
    private String orderRemark;

    @ApiModelProperty("运输类型")
    private Integer businessTypeCode;

    @ApiModelProperty(value = "业务类型", hidden = true)
    private Long containerBusinessTypeId;

    @ApiModelProperty("业务类型")
    private String containerBusinessTypeName;

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

    @ApiModelProperty("柜型")
    private Long cabinetId;

    @ApiModelProperty("柜型名称")
    private String cabinetName;

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

    @ApiModelProperty("税点")
    private BigDecimal taxPoint;

    @ApiModelProperty("本单利润")
    private BigDecimal profitAmount;

    @ApiModelProperty("本单税金")
    private BigDecimal taxAmount;

    @ApiModelProperty("应收总金额")
    private BigDecimal receiveAmount;

    @ApiModelProperty("应付总金额")
    private BigDecimal payableAmount;

    @ApiModelProperty("应付现金总金额")
    private BigDecimal payableCashAmount;

    @ApiModelProperty("应付油卡总金额")
    private BigDecimal payableOilCardAmount;

    @ApiModelProperty("司机工资")
    private BigDecimal salaryAmount;

    @ApiModelProperty("在途费用")
    private BigDecimal carCostAmount;

    @ApiModelPropertyEnum(value = PayOrderStatusEnum.class, desc = "付款状态")
    private Integer payStatus;

    @ApiModelPropertyEnum(value = PayOrderStatusEnum.class, desc = "现金部分付款状态")
    private Integer cashPayStatus;

    @ApiModelPropertyEnum(value = PayOrderStatusEnum.class, desc = "油卡部分付款状态")
    private Integer oilCardPayStatus;

    @ApiModelProperty("是否提交过收款")
    private Boolean submitReceiveFlag;

    @ApiModelProperty("发货日期")
    private LocalDateTime deliverGoodsTime;

    @ApiModelProperty("收货日期")
    private LocalDateTime receiveGoodsTime;

    @ApiModelProperty("装货磅单重量")
    private BigDecimal loadPoundListQuantity;

    @ApiModelProperty("卸货磅单重量")
    private BigDecimal unloadPoundListQuantity;

    @ApiModelProperty("装货磅单凭证")
    @JsonSerialize(using = FileKeyVoSerializer.class)
    private String loadPoundListAttachment;

    @ApiModelProperty("卸货磅单凭证")
    @JsonSerialize(using = FileKeyVoSerializer.class)
    private String unloadPoundListAttachment;


    @ApiModelProperty("提箱地点和相关联系人")
    private String containerLocation;

    @ApiModelProperty("装货地点和相关联系人")
    private String placingLocation;

    @ApiModelProperty("卸货地点和相关联系人")
    private String unloadingLocation;

    @ApiModelProperty("还箱地点和相关联系人")
    private String returnContainerLocation;

    @ApiModelProperty("路线名称")
    private String routeName;

    @ApiModelProperty("流程实例id")
    private Long flowInstanceId;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("合同文件")
    @JsonSerialize(using = FileKeyVoSerializer.class)
    private String contractFile;

    @ApiModelProperty("合同编号")
    private String contractCode;

    @ApiModelProperty("运单-创建人id")
    private Long createUserId;

    @ApiModelProperty("运单-创建人名称")
    private String createUserName;

    @ApiModelProperty("货主业务负责人ID")
    private Long shipperManagerId;

    @ApiModelProperty("货主业务负责人名称")
    private String shipperManagerName;

    @ApiModelProperty("订单业务负责人ID")
    private Long orderManagerId;

    @ApiModelProperty("订单业务负责人名称")
    private String orderManagerName;

    @ApiModelProperty("客服-订单-创建人id")
    private Long orderCreateUserId;

    @ApiModelProperty("客服-订单-创建人名称")
    private String orderCreateUserName;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("调度ID")
    private Long scheduleId;

    @ApiModelProperty("调度人名称")
    private String scheduleName;

    @ApiModelProperty("订单最迟提箱时间")
    private LocalDateTime latestPackingTime;

    @ApiModelProperty("回单附件")
    @JsonSerialize(using = FileKeyVoSerializer.class)
    @JsonDeserialize(using = FileKeyVoDeserializer.class)
    private String receiptAttachment;

    @ApiModelProperty("派车单附件")
    @JsonSerialize(using = FileKeyVoSerializer.class)
    @JsonDeserialize(using = FileKeyVoDeserializer.class)
    private String truckOrderAttachment;

    @ApiModelProperty("核算附件")
    @JsonSerialize(using = FileKeyVoSerializer.class)
    @JsonDeserialize(using = FileKeyVoDeserializer.class)
    private String attachment;

    @ApiModelPropertyEnum(value = WaybillTransportModeEnum.class, desc = "运输方式")
    private Integer transportMode;

    @ApiModelProperty("业务时间")
    private LocalDate businessDate;

    @ApiModelProperty("异常列表")
    private List<WaybillExceptionVO> exceptionList;

    @ApiModelProperty("路线信息")
    private List<WaybillPathVO> pathList;
}
