package net.lab1024.tms.common.module.business.pay.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.lab1024.tms.common.module.business.flow.FlowAuditStatusEnum;
import net.lab1024.tms.common.module.business.order.constant.OrderTypeEnum;
import net.lab1024.tms.common.module.business.pay.constant.PayOrderStatusEnum;
import net.lab1024.tms.common.module.business.waybill.constant.WaybillSettleTypeEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 付款单：运单结算，生成付款单
 * @author yandy
 */
@Data
@TableName("t_pay_order")
public class  PayOrderEntity {


    @TableId(type = IdType.AUTO)
    private Long payOrderId;

    /**
     * 付款单号
     */
    private String payOrderNumber;

    /**
     * @see net.lab1024.tms.common.module.business.pay.constant.PayOrderTypeEnum
     */
    private Integer payOrderType;

    private Long orderId;


    /**
     * 订单类型
     *
     * @see OrderTypeEnum
     */
    private Integer orderType;


    private Long waybillId;


    /**
     * 是否网络货运公司 推单企业
     */
    private Long nftEnterpriseId;

    /**
     * 企业ID
     */
    private Long enterpriseId;

    /**
     * 货主id
     */
    private Long shipperId;

    /**
     * 司机id
     */
    private Long driverId;

    private Long vehicleId;
    /**
     * 车队id
     */
    private Long fleetId;

    /**
     * 油卡id
     */
    private Long oilCardId;

    /**
     * 应付
     */
    private BigDecimal payableTotalAmount;

    /**
     * 已付
     */
    private BigDecimal paidTotalAmount;

    /**
     * 支付凭证
     */
    private String attachment;

    private String sequenceCode;

    /**
     * 付款时间
     */
    private LocalDateTime payTime;

    /**
     * 审核状态
     * @see FlowAuditStatusEnum
     */
    private Integer auditStatus;

    /**
     * 付款单状态
     * @see PayOrderStatusEnum
     */
    private Integer payOrderStatus;

    /**
     * 是否核销
     */
    private Boolean verificationFlag;

    private String remark;

    /**
     * 流程实例id
     */
    private Long flowInstanceId;

    private Long createUserId;

    private String createUserName;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

}
