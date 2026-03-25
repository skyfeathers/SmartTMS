package net.lab1024.tms.admin.module.business.waybill.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.domain.PageParam;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.module.business.flow.FlowAuditStatusEnum;
import net.lab1024.tms.common.module.business.order.constant.OrderTypeEnum;
import net.lab1024.tms.common.module.business.pay.constant.PayOrderStatusEnum;
import net.lab1024.tms.common.module.business.waybill.constant.WaybillExceptionTypeEnum;
import net.lab1024.tms.common.module.business.waybill.constant.WaybillSettleModeEnum;
import net.lab1024.tms.common.module.business.waybill.constant.WaybillSettleTypeEnum;
import net.lab1024.tms.common.module.business.waybill.constant.WaybillStatusEnum;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * @author zhuoda
 * @Date 2022-07-14
 */
@Data
public class WaybillQueryForm extends PageParam {

    @ApiModelProperty("运单号/订单号/箱号铅封号")
    private String keywords;

    @ApiModelProperty("运单号进行检索")
    private String waybillNumber;

    @ApiModelProperty("支持单、多个运单号进行检索")
    private String waybillNumbers;

    @ApiModelProperty(value = "运单号", hidden = true)
    private List<String> waybillNumberList;

    @ApiModelProperty("订单号")
    private String orderNo;

    @ApiModelProperty("客户订单号")
    private String shipperOrderNumber;

    @ApiModelProperty("箱号")
    private String containerNumber;

    @ApiModelProperty("铅封号")
    private String leadSealNumber;

    @ApiModelProperty("柜型")
    private Long cabinetId;

    @ApiModelProperty("最迟装货时间开始时间")
    private LocalDate latestPackingTimeStart;

    @ApiModelProperty("最迟装货时间结束时间")
    private LocalDate latestPackingTimeEnd;

    @ApiModelProperty("是否提交过支付过")
    private Boolean submitPayFlag;

    @ApiModelProperty("是否提交过收款")
    private Boolean submitReceiveFlag;

    @ApiModelProperty("提箱地点")
    private String containerLocation;

    @ApiModelProperty("装货地点")
    private String placingLocation;

    @ApiModelProperty("卸货地点")
    private String unloadingLocation;

    @ApiModelProperty("还箱地点")
    private String returnContainerLocation;

    @ApiModelProperty("运单id列表")
    private List<Long> waybillIdList;

    @ApiModelProperty("货主ID")
    private Long shipperId;

    @ApiModelProperty("司机")
    private Long driverId;

    @ApiModelProperty("司机ID列表")
    private List<Long> driverIdList;

    @ApiModelProperty("车辆")
    private Long vehicleId;

    @ApiModelProperty("车辆ID列表")
    private List<Long> vehicleIdList;

    @ApiModelProperty("车辆类型")
    private String vehicleType;

    @ApiModelProperty("车辆经营方式")
    private List<Integer> vehicleBusinessMode;

    @ApiModelProperty("车队")
    private Long fleetId;

    @ApiModelPropertyEnum(desc = "订单类型", value = OrderTypeEnum.class)
    private Integer orderType;

    @ApiModelPropertyEnum(desc = "审核状态", value = FlowAuditStatusEnum.class)
    private Integer auditStatus;

    @ApiModelProperty("运单状态")
    @ApiModelPropertyEnum(WaybillStatusEnum.class)
    private Integer waybillStatus;

    @ApiModelProperty("付款状态")
    @ApiModelPropertyEnum(PayOrderStatusEnum.class)
    private Integer payStatus;

    @ApiModelPropertyEnum(value = PayOrderStatusEnum.class, desc = "现金部分付款状态", hidden = true)
    private Integer cashPayStatus;

    @ApiModelPropertyEnum(value = PayOrderStatusEnum.class, desc = "油卡部分付款状态")
    private Integer oilCardPayStatus;


    @ApiModelProperty("运单生成开始时间")
    private LocalDate startTime;

    @ApiModelProperty("运单生成截止时间")
    private LocalDate endTime;

    @ApiModelPropertyEnum(value = WaybillSettleTypeEnum.class, desc = "结算类型")
    private Integer settleType;

    @ApiModelProperty("是否隐藏作废单据")
    private Boolean hideCancelFlag;

    @ApiModelPropertyEnum(value = WaybillSettleModeEnum.class, desc = "结算类型")
    private Integer settleMode;

    @ApiModelProperty(value = "排除的运单状态", hidden = true)
    private Integer excludeStatus;

    @ApiModelProperty("所属销售")
    private List<Long> managerIdList;

    @ApiModelProperty("创建人角色id")
    private Long createUserRoleId;

    @ApiModelProperty(value = "创建人id", hidden = true)
    private List<Long> createUserIdList;

    @ApiModelProperty("运输方式")
    private Integer transportMode;

    @ApiModelPropertyEnum(value = WaybillExceptionTypeEnum.class, desc = "异常类型")
    private List<Integer> exceptionTypeList;

    @ApiModelProperty("装货-开始时间")
    private LocalDate startLoadTime;

    @ApiModelProperty("装货-截止时间")
    private LocalDate endLoadTime;

    @ApiModelProperty("卸货-开始时间")
    private LocalDate startUnloadTime;

    @ApiModelProperty("卸货-截止时间")
    private LocalDate endUnloadTime;

    @ApiModelProperty("发货时间-开始时间")
    private LocalDate deliverGoodsTimeStart;

    @ApiModelProperty("发货时间-截止时间")
    private LocalDate deliverGoodsTimeEnd;

    @ApiModelProperty("收货时间-开始时间")
    private LocalDate receiveGoodsTimeStart;

    @ApiModelProperty("收货时间-截止时间")
    private LocalDate receiveGoodsTimeEnd;

    @ApiModelProperty("业务时间-月份开始时间")
    private LocalDate businessStartDate;

    @ApiModelProperty("业务时间-月份结束时间")
    private LocalDate businessEndDate;

    @ApiModelProperty("发货日期-开始时间")
    private LocalDate deliverGoodsStartTime;

    @ApiModelProperty("发货日期-结束时间")
    private LocalDate deliverGoodsEndTime;

    @ApiModelProperty("调度ID列表")
    private List<Long> scheduleIdList;

    @ApiModelProperty("客服ID列表")
    private List<Long> customerIdList;

    @ApiModelProperty(value = "是否分段运输")
    private Boolean splitTransportFlag;

    @ApiModelProperty("利润区间开始")
    private BigDecimal startProfitAmount;
    @ApiModelProperty("利润区间结束")
    private BigDecimal endProfitAmount;

    @ApiModelProperty("运单所属公司")
    private Long enterpriseId;
}
