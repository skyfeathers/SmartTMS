package net.lab1024.tms.common.module.business.order.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.lab1024.tms.common.module.business.material.transportroute.constants.TransportationTypeEnum;
import net.lab1024.tms.common.module.business.order.constant.OrderStatusEnum;
import net.lab1024.tms.common.module.business.order.constant.OrderTypeEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单
 *
 * @author lidoudou
 * @date 2022/7/11 下午5:31
 */
@Data
@TableName("t_order")
public class OrderEntity {

    @TableId(type = IdType.AUTO)
    private Long orderId;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 是否分段运输
     */
    private Boolean splitTransportFlag;

    /**
     * 货主ID
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
     * 调度员
     */
    private Long scheduleId;

    /**
     * 所属区域
     */
    private String areaId;

    /**
     * 业务类型
     *
     * @see TransportationTypeEnum
     */
    private Integer businessTypeCode;

    /**
     * 业务类型
     */
    private Long containerBusinessTypeId;

    /**
     * 柜型
     */
    private Long cabinetId;

    /**
     * 运输距离 单位公里
     */
    private BigDecimal distance;

    /**
     * 状态
     *
     * @see OrderStatusEnum
     */
    private Integer status;

    /**
     * 提箱地点
     */
    private String containerLocation;

    /**
     * 装货地点
     */
    private String placingLocation;

    /**
     * 卸货地点
     */
    private String unloadingLocation;

    /**
     * 还箱地点
     */
    private String returnContainerLocation;

    /**
     * 运输路线ID
     */
    private Long transportRouteId;

    /**
     * 备注
     */
    private String remark;

    /**
     * 单趟应收金额
     */
    private BigDecimal singleTripReceiveAmount;

    /**
     * 单趟应付金额
     */
    private BigDecimal singleTripFreightAmount;

    /**
     * 税点
     */
    private BigDecimal taxPoint;

    /**
     * 分配状态 未分配 已分配
     */
    private Boolean scheduleFlag;


    /**
     * 装货时间
     */
    private LocalDateTime loadTime;

    private LocalDateTime unloadTime;

    /**
     * 客户联系人
     */
    private String shipperContact;

    /**
     * 客户订单号
     */
    private String shipperOrderNumber;

    /**
     * 最迟提箱时间
     */
    private LocalDateTime latestPackingTime;

    /**
     * 业务负责人 - 销售
     */
    private Long managerId;

    /**
     * 删除状态
     */
    private Boolean deletedFlag;

    private Long createUserId;

    private String createUserName;

    private Long updateUserId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
