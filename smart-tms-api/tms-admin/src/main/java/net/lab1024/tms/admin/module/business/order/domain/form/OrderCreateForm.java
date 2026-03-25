package net.lab1024.tms.admin.module.business.order.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.admin.module.business.order.domain.dto.OrderGoodsDTO;
import net.lab1024.tms.admin.module.business.order.domain.dto.OrderMailAddressDTO;
import net.lab1024.tms.admin.module.business.order.domain.dto.OrderPathDTO;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.module.business.material.transportroute.constants.TransportationTypeEnum;
import net.lab1024.tms.common.module.business.order.constant.OrderTypeEnum;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 新建订单
 *
 * @author lidoudou
 * @date 2022/7/13 上午11:
 */
@Data
public class OrderCreateForm {

    @ApiModelProperty("客户名称")
    @NotNull(message = "请选择客户")
    private Long shipperId;

    @ApiModelProperty(value = "企业ID",hidden = true)
    private Long enterpriseId;

    @ApiModelProperty(value = "是否分段运输")
    private Boolean splitTransportFlag;

    @NotNull(message = "网络货运平台订单不能为空")
    @ApiModelPropertyEnum(value = OrderTypeEnum.class, desc="是否为网络货运付款单")
    private Integer orderType;

    @ApiModelProperty("推单企业")
    private Long nftEnterpriseId;

    @ApiModelProperty("调度员")
    @NotNull(message = "请选择调度员")
    private Long scheduleId;

    @ApiModelProperty("所属区域 取字典表内容")
    private String areaId;

    @ApiModelPropertyEnum(value = TransportationTypeEnum.class, desc = "业务类型")
    @NotNull(message = "运输类型不能为空")
    private Integer businessTypeCode;

    @ApiModelProperty("业务类型")
    @NotNull(message = "业务类型不能为空")
    private Long containerBusinessTypeId;

    @ApiModelProperty("柜型")
    private Long cabinetId;

    @ApiModelProperty("运输距离 单位公里")
    private BigDecimal distance;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("税点")
    @NotNull(message = "税点不能为空")
    private BigDecimal taxPoint;

    @ApiModelProperty("装货时间")
    private LocalDateTime loadTime;

    @ApiModelProperty("卸货时间")
    private LocalDateTime unloadTime;

    @ApiModelProperty("运输路线")
    @NotNull(message = "运输路线不能为空")
    @Size(min = 1, message = "运输路线不能为空")
    @Valid
    private List<OrderPathDTO> pathList;

    @ApiModelProperty("货物信息")
    @Valid
    @NotNull(message = "货物信息不能为空")
    @Size(min = 1, message = "货物信息不能为空")
    private List<OrderGoodsDTO> goodsList;

    @ApiModelProperty("运输路线名称")
    private String transportRouteName;

    @ApiModelProperty("运输路线ID")
    private Long transportRouteId;

    @ApiModelProperty("客户联系人")
    private String shipperContact;

    @ApiModelProperty("客户订单号")
    private String shipperOrderNumber;

    @ApiModelProperty("最迟提箱时间")
    private LocalDateTime latestPackingTime;

    @ApiModelProperty("单趟应收费用")
    private BigDecimal singleTripReceiveAmount;

    @ApiModelProperty("单趟运费费用")
    private BigDecimal singleTripFreightAmount;

    @ApiModelProperty(hidden = true)
    private Long createUserId;

    @ApiModelProperty(value = "业务负责人", hidden = true)
    private Long managerId;

    @ApiModelProperty(value = "创建人", hidden = true)
    private String createUserName;
}
