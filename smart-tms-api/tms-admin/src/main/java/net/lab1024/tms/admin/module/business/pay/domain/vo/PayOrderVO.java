package net.lab1024.tms.admin.module.business.pay.domain.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.deserializer.FileKeyVoDeserializer;
import net.lab1024.tms.common.common.serializer.FileKeyVoSerializer;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.module.business.flow.FlowAuditStatusEnum;
import net.lab1024.tms.common.module.business.order.constant.OrderTypeEnum;
import net.lab1024.tms.common.module.business.pay.constant.PayOrderStatusEnum;
import net.lab1024.tms.common.module.business.pay.constant.PayOrderTypeEnum;
import net.lab1024.tms.common.module.business.waybill.constant.WaybillSettleTypeEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author yandy
 * @description:
 * @date 2022/8/15 11:53 上午
 */
@Data
public class PayOrderVO {

    @ApiModelProperty("付款单id")
    private Long payOrderId;

    @ApiModelProperty("付款单号")
    private String payOrderNumber;

    @ApiModelPropertyEnum(PayOrderTypeEnum.class)
    private Integer payOrderType;

    @ApiModelProperty("订单id")
    private Long orderId;

    @ApiModelProperty("调度员id")
    private Long scheduleId;

    @ApiModelProperty("调度员名称")
    private String scheduleName;

    @ApiModelPropertyEnum(value = OrderTypeEnum.class, desc="是否为网络货运付款单")
    private Integer orderType;

    private Long shipperId;

    private String shipperName;

    @ApiModelProperty("运单id")
    private Long waybillId;

    @ApiModelProperty("运单号")
    private String waybillNumber;

    @ApiModelProperty("网络货运-公司id")
    private Long nftEnterpriseId;

    @ApiModelProperty("网络货运-公司名称")
    private String nftEnterpriseName;

    @ApiModelProperty("所属公司id")
    private Long enterpriseId;

    @ApiModelProperty("所属公司名称")
    private String enterpriseName;

    @ApiModelProperty("箱号")
    private String containerNumber;

    @ApiModelProperty("油卡id")
    private Long oilCardId;

    @ApiModelProperty("油卡卡号")
    private String oilCardNo;

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

    @ApiModelProperty("车队长名称")
    private String fleetCaptainName;

    @ApiModelProperty("车队长电话")
    private String fleetCaptainPhone;

    @ApiModelPropertyEnum(PayOrderStatusEnum.class)
    private Integer payOrderStatus;

    @ApiModelPropertyEnum(FlowAuditStatusEnum.class)
    private Integer auditStatus;

    @ApiModelProperty("支付凭证")
    @JsonSerialize(using = FileKeyVoSerializer.class)
    private String attachment;

    @ApiModelProperty("支付流水")
    private String sequenceCode;

    @ApiModelProperty("是否核销")
    private Boolean verificationFlag;

    @ApiModelProperty("审批id")
    private Long flowInstanceId;

    @ApiModelProperty("应付金额")
    private BigDecimal payableTotalAmount;

    @ApiModelProperty("已付金额")
    private BigDecimal paidTotalAmount;

    @ApiModelProperty("付款备注")
    private String remark;

    @ApiModelProperty("创建人id")
    private Long createUserId;

    @ApiModelProperty("创建人名称")
    private String createUserName;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("收款信息")
    private PayOrderReceiveVO receiveVO;

    @ApiModelProperty("付款信息")
    private PayOrderPaymentVO paymentVO;

    @ApiModelProperty("核销信息")
    private PayOrderVerificationVO verificationVO;

    @ApiModelProperty("是否可审核")
    private Boolean auditFlag;

    @ApiModelProperty("当前审核人")
    private String currentAuditor;

    @ApiModelProperty("最迟提箱时间")
    private LocalDateTime latestPackingTime;

    @ApiModelProperty("发货时间")
    private LocalDateTime driverGoodsTime;

    @ApiModelProperty("收货时间")
    private LocalDateTime receiveGoodsTime;

    @ApiModelProperty("运单创建时间")
    private LocalDateTime waybillCreateTime;

    @ApiModelProperty("回单附件")
    @JsonSerialize(using = FileKeyVoSerializer.class)
    private String receiptAttachment;

    @ApiModelProperty("派车单附件")
    @JsonSerialize(using = FileKeyVoSerializer.class)
    @JsonDeserialize(using = FileKeyVoDeserializer.class)
    private String truckOrderAttachment;
}
