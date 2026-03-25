package net.lab1024.tms.admin.module.business.order.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.admin.module.business.order.domain.dto.OrderGoodsDTO;
import net.lab1024.tms.admin.module.business.order.domain.dto.OrderMailAddressDTO;
import net.lab1024.tms.admin.module.business.order.domain.dto.OrderPathDTO;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.module.business.material.transportroute.constants.TransportationTypeEnum;
import net.lab1024.tms.common.module.business.order.constant.OrderStatusEnum;
import net.lab1024.tms.common.module.business.order.constant.OrderTypeEnum;
import net.lab1024.tms.common.module.support.datatracer.annoation.DataTracerFieldDoc;
import net.lab1024.tms.common.module.support.datatracer.annoation.DataTracerFieldEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单详情
 *
 * @author lidoudou
 * @date 2022/7/13 下午3:25
 */
@Data
public class OrderVO {

    @ApiModelProperty("订单ID")
    private Long orderId;

    @ApiModelProperty("订单编号")
    private String orderNo;

    @ApiModelProperty("货主ID")
    private Long shipperId;

    @ApiModelProperty("货主名称")
    private String consignor;

    @ApiModelProperty("货主简称")
    @DataTracerFieldDoc("货主简称")
    private String shortName;

    @ApiModelProperty("企业ID")
    private Long enterpriseId;

    @ApiModelProperty("企业名称")
    @DataTracerFieldDoc("所属公司")
    private String enterpriseName;

    @ApiModelProperty("调度员")
    private Long scheduleId;

    @ApiModelProperty("调度员名称")
    @DataTracerFieldDoc("调度员")
    private String scheduleName;

    @ApiModelProperty(value = "是否分段运输")
    private Boolean splitTransportFlag;

    @DataTracerFieldDoc("订单类型")
    @DataTracerFieldEnum(enumClass = OrderTypeEnum.class)
    @ApiModelPropertyEnum(value = OrderTypeEnum.class, desc = "是否为网络货运付款单")
    private Integer orderType;

    @ApiModelProperty("推单企业")
    private Long nftEnterpriseId;

    @ApiModelProperty("所属区域 取字典表内容")
    private String areaId;

    @ApiModelProperty("所属区域描述")
    @DataTracerFieldDoc("所属区域")
    private String areaDesc;

    @ApiModelPropertyEnum(desc = "运输类型", value = TransportationTypeEnum.class)
    @DataTracerFieldEnum(enumClass = TransportationTypeEnum.class)
    private Integer businessTypeCode;

    @ApiModelProperty("业务类型")
    private Long containerBusinessTypeId;

    @ApiModelProperty("业务类型名称")
    @DataTracerFieldDoc("业务类型")
    private String containerBusinessTypeName;

    @ApiModelProperty("柜型")
    private Long cabinetId;

    @ApiModelProperty("柜型名称")
    @DataTracerFieldDoc("柜型")
    private String cabinetName;

    @ApiModelProperty("运输距离 单位公里")
    private BigDecimal distance;

    @ApiModelPropertyEnum(desc = "订单状态", value = OrderStatusEnum.class)
    private Integer status;

    @ApiModelProperty("提箱地点")
    private String containerLocation;

    @ApiModelProperty("装货地点")
    private String placingLocation;

    @ApiModelProperty("卸货地点")
    private String unloadingLocation;

    @ApiModelProperty("还箱地点")
    private String returnContainerLocation;

    @ApiModelProperty("运输路线名称")
    private String transportRouteName;

    @ApiModelProperty("运输路线ID")
    private Long transportRouteId;

    @ApiModelProperty("单趟应收费用")
    private BigDecimal singleTripReceiveAmount;

    @ApiModelProperty("单趟运费费用")
    private BigDecimal singleTripFreightAmount;

    @ApiModelProperty("备注")
    @DataTracerFieldDoc("备注")
    private String remark;

    @ApiModelProperty("创建人")
    private String createUserName;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;


    @ApiModelProperty("税点")
    @DataTracerFieldDoc("税点")
    private BigDecimal taxPoint;

    @ApiModelProperty("装货时间")
    private LocalDateTime loadTime;

    @ApiModelProperty("卸货时间")
    private LocalDateTime unloadTime;

    @ApiModelProperty("路线信息")
    private List<OrderPathDTO> pathList;

    @ApiModelProperty("货物信息")
    private List<OrderGoodsVO> goodsList;

    @ApiModelProperty("是否分配")
    private Boolean scheduleFlag;

    @ApiModelProperty("客户联系人")
    private String shipperContact;

    @ApiModelProperty("客户订单号")
    private String shipperOrderNumber;

    @ApiModelProperty("最迟提箱时间")
    private LocalDateTime latestPackingTime;

    @ApiModelProperty("业务负责人")
    private Long managerId;

    @ApiModelProperty("业务负责人-姓名")
    @DataTracerFieldDoc("业务负责人")
    private String managerName;

}
