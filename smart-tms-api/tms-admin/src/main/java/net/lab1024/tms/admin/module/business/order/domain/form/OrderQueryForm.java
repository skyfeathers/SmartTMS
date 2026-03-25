package net.lab1024.tms.admin.module.business.order.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.domain.PageParam;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.module.business.material.transportroute.constants.TransportationTypeEnum;
import net.lab1024.tms.common.module.business.order.constant.OrderStatusEnum;
import net.lab1024.tms.common.module.business.order.constant.OrderTypeEnum;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;
import java.util.List;

/**
 * 订单查询参数
 *
 * @author lidoudou
 * @date 2022/7/13 下午3:17
 */
@Data
public class OrderQueryForm extends PageParam {

    @ApiModelProperty("关键字")
    @Length(max = 200, message = "关键字最多200字符")
    private String keywords;

    @ApiModelProperty("订单编号")
    @Length(max = 200, message = "订单编号最多200字符")
    private String orderNo;

    @ApiModelProperty("客户联系人")
    @Length(max = 200, message = "客户联系人最多200字符")
    private String shipperContact;

    @ApiModelProperty("货主ID")
    private Long shipperId;

    @ApiModelProperty("货主名称")
    private String consignor;

    @ApiModelProperty("货主简称")
    private String shortName;

    @ApiModelPropertyEnum(desc = "运输类型", value = TransportationTypeEnum.class)
    private Integer businessTypeCode;

    @ApiModelPropertyEnum(desc = "订单类型", value = OrderTypeEnum.class)
    private Integer orderType;

    @ApiModelProperty("货物类型 取字典表内容")
    private String cargoTypeClassificationCode;

    @ApiModelProperty("业务类型")
    private Long containerBusinessTypeId;

    @ApiModelPropertyEnum(desc = "状态", value = OrderStatusEnum.class)
    private Integer status;

    @ApiModelProperty("所属区域 取字典表内容")
    private String areaId;

    @ApiModelProperty("柜型")
    private Long cabinetId;

    @ApiModelProperty("开始时间")
    private LocalDate startTime;

    @ApiModelProperty("结束时间")
    private LocalDate endTime;

    @ApiModelProperty("提箱地点")
    private String containerLocation;

    @ApiModelProperty("装货地点")
    private String placingLocation;

    @ApiModelProperty("卸货地点")
    private String unloadingLocation;

    @ApiModelProperty("还箱地点")
    private String returnContainerLocation;

    @ApiModelProperty("创建人")
    private String createUserName;

    @ApiModelProperty("调度员")
    private Long scheduleId;

    @ApiModelProperty("订单所属公司")
    private Long enterpriseId;

    @ApiModelProperty("是否分配运单")
    private Boolean scheduleFlag;

    @ApiModelProperty("是否隐藏作废单据")
    private Boolean hideCancelFlag;

    @ApiModelProperty("销售id")
    private List<Long> managerIdList;

    @ApiModelProperty(value = "是否分段运输")
    private Boolean splitTransportFlag;

    @ApiModelProperty(value = "删除状态", hidden = true)
    private Boolean deletedFlag;

    @ApiModelPropertyEnum(value = OrderStatusEnum.class, desc = "订单状态", hidden = true)
    private Integer excludeOrderStatus;

    @ApiModelProperty("最迟装货时间开始时间")
    private LocalDate latestPackingTimeStart;

    @ApiModelProperty("最迟装货时间结束时间")
    private LocalDate latestPackingTimeEnd;

    @ApiModelProperty("装货时间开始时间")
    private LocalDate loadTimeStart;

    @ApiModelProperty("装货时间结束时间")
    private LocalDate loadTimeEnd;

    @ApiModelProperty("卸货时间开始时间")
    private LocalDate unloadTimeStart;

    @ApiModelProperty("卸货时间结束时间")
    private LocalDate unloadTimeEnd;

}
