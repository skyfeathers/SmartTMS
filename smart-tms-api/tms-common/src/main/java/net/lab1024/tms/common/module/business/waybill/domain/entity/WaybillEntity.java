package net.lab1024.tms.common.module.business.waybill.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.lab1024.tms.common.module.business.flow.FlowAuditStatusEnum;
import net.lab1024.tms.common.module.business.order.constant.OrderTypeEnum;
import net.lab1024.tms.common.module.business.waybill.constant.WaybillSettleModeEnum;
import net.lab1024.tms.common.module.business.waybill.constant.WaybillSettleTypeEnum;
import net.lab1024.tms.common.module.business.waybill.constant.WaybillStatusEnum;
import net.lab1024.tms.common.module.business.waybill.constant.WaybillTransportModeEnum;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author zhuoda
 * @Date 2022-07-13
 */
@TableName("t_waybill")
@Data
public class WaybillEntity {

    /**
     * 运单自制id
     */
    @TableId(type = IdType.AUTO)
    private Long waybillId;

    /**
     * 运单号
     */
    private String waybillNumber;

    /**
     * 是否分段运输
     */
    private Boolean splitTransportFlag;

    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 货主id
     */
    private Long shipperId;

    /**
     * 企业ID
     */
    private Long enterpriseId;

    /**
     * 订单类型
     *
     * @see OrderTypeEnum
     */
    private Integer orderType;

    /**
     * 是否网络货运公司 推单企业
     */
    private Long nftEnterpriseId;

    /**
     * 铅封
     */
    private String leadSealNumber;

    /**
     * 箱号
     */
    private String containerNumber;

    /**
     * 运单状态
     *
     * @see WaybillStatusEnum
     */
    private Integer waybillStatus;

    /**
     * 审核状态状态
     *
     * @see FlowAuditStatusEnum
     */
    private Integer auditStatus;

    /**
     * 结算类型
     *
     * @see WaybillSettleTypeEnum
     */
    private Integer settleType;
    /**
     * 结算方式
     *
     * @see WaybillSettleModeEnum
     */
    private Integer settleMode;

    /**
     * 司机
     */
    private Long driverId;

    /**
     * 车辆
     */
    private Long vehicleId;

    /**
     * 车队id
     */
    private Long fleetId;

    /**
     * 是否提交过收款
     */
    private Boolean submitReceiveFlag;

    private BigDecimal taxPoint;

    /**
     * 应收总金额
     */
    private BigDecimal receiveAmount;

    /**
     * 应付总金额
     */
    private BigDecimal payableAmount;

    /**
     * 司机工资
     */
    private BigDecimal salaryAmount;

    /**
     * 在途费用
     */
    private BigDecimal carCostAmount;

    /**
     * 运单利润 应收金额 - 应付金额 - 税金
     */
    private BigDecimal profitAmount;

    /**
     * 付款状态
     * @see net.lab1024.tms.common.module.business.pay.constant.PayOrderStatusEnum
     */
    private Integer payStatus;

    /**
     * 应付现金总金额
     */
    private BigDecimal payableCashAmount;

    /**
     * 现金付款状态
     * @see net.lab1024.tms.common.module.business.pay.constant.PayOrderStatusEnum
     */
    private Integer cashPayStatus;

    /**
     * 应付油卡总金额
     */
    private BigDecimal payableOilCardAmount;

    /**
     * 油卡付款状态
     * @see net.lab1024.tms.common.module.business.pay.constant.PayOrderStatusEnum
     */
    private Integer oilCardPayStatus;



    /**
     * 备注
     */
    private String remark;


    /**
     * 发货日期
     */
    private LocalDateTime deliverGoodsTime;

    /**
     * 收货日期
     */
    private LocalDateTime receiveGoodsTime;

    /**
     * 装货磅单重量
     */
    private BigDecimal loadPoundListQuantity;

    /**
     * 卸货货磅单重量
     */
    private BigDecimal unloadPoundListQuantity;

    /**
     * 装货时间
     */
    private LocalDateTime loadTime;

    /**
     * 卸货时间
     */
    private LocalDateTime unloadTime;

    /**
     * 完成时间
     */
    private LocalDateTime completeTime;

    /**
     * 路线名字
     */
    private String routeName;

    /**
     * 运输距离 单位公里
     */
    private BigDecimal distance;

    /**
     * 回单附件
     */
    private String receiptAttachment;

    /**
     * 派车单附件
     */
    private String truckOrderAttachment;

    /**
     * 业务时间
     */
    private LocalDate businessDate;

    /**
     * 业务类型
     *
     * {@link WaybillTransportModeEnum}
     */
    private Integer transportMode;

    /**
     * 流程实例id
     */
    private Long flowInstanceId;

    private Long createUserId;

    private String createUserName;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;


}
