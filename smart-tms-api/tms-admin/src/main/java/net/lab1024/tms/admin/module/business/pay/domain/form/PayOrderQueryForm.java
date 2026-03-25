package net.lab1024.tms.admin.module.business.pay.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.domain.PageParam;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.common.validator.enumeration.CheckEnum;
import net.lab1024.tms.common.module.business.flow.FlowAuditStatusEnum;
import net.lab1024.tms.common.module.business.order.constant.OrderTypeEnum;
import net.lab1024.tms.common.module.business.pay.constant.PayOrderStatusEnum;
import net.lab1024.tms.common.module.business.pay.constant.PayOrderTypeEnum;
import net.lab1024.tms.common.module.business.waybill.constant.WaybillSettleTypeEnum;

import java.time.LocalDate;
import java.util.List;

/**
 * 查询付款单
 *
 * @author yandy
 */
@Data
public class PayOrderQueryForm extends PageParam {

    @ApiModelProperty("关键字")
    private String keywords;

    @ApiModelProperty("支持单、多个运单号进行检索")
    private String waybillNumbers;

    @ApiModelProperty(value = "运单号", hidden = true)
    private List<String> waybillNumberList;

    @ApiModelProperty(value = "企业ID",hidden = true)
    private Long enterpriseId;

    @ApiModelPropertyEnum(value = FlowAuditStatusEnum.class, desc = "审核状态")
    @CheckEnum(value = FlowAuditStatusEnum.class, message = "审核状态错误")
    private Integer auditStatus;

    @ApiModelPropertyEnum(PayOrderStatusEnum.class)
    private Integer payOrderStatus;

    @ApiModelPropertyEnum(PayOrderTypeEnum.class)
    private Integer payOrderType;

    @ApiModelProperty("货主Id")
    private Long shipperId;

    @ApiModelProperty("司机Id")
    private Long driverId;

    @ApiModelProperty("车辆Id")
    private Long vehicleId;

    @ApiModelProperty("车队Id")
    private Long fleetId;

    @ApiModelProperty("是否核销")
    private Boolean verificationFlag;

    @ApiModelProperty("创建人ID")
    private Long createUserId;

    @ApiModelProperty("开始时间")
    private LocalDate startTime;

    @ApiModelProperty("结束时间")
    private LocalDate endTime;

    @ApiModelProperty("支付开始时间")
    private LocalDate payStartTime;

    @ApiModelProperty("支付结束时间")
    private LocalDate payEndTime;

    @ApiModelProperty("由我审核的")
    private Boolean auditByMeFlag;

    @ApiModelProperty("待我审核")
    private Boolean waitAuditByMeFlag;

    @ApiModelPropertyEnum(value = FlowAuditStatusEnum.class, desc = "审核状态", hidden = true)
    private List<Integer> auditStatusList;

    @ApiModelPropertyEnum(value = PayOrderStatusEnum.class, desc = "付款单状态", hidden = true)
    private Integer excludePayOrderStatus;

    @ApiModelProperty(value = "当前登录人", hidden = true)
    private Long requestUserId;
}
