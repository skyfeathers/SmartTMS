package net.lab1024.tms.admin.module.business.waybill.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.common.validator.enumeration.CheckEnum;
import net.lab1024.tms.common.module.business.order.constant.OrderTypeEnum;
import net.lab1024.tms.common.module.business.waybill.constant.WaybillTransportModeEnum;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author zhuoda
 * @Date 2022-07-13
 */
@Data
public class WayBillCreateForm extends WaybillSettleTypeForm {

    @ApiModelProperty("订单id")
    @NotNull(message = "订单id不能为空")
    private Long orderId;


    @ApiModelProperty("收款")
    @NotNull(message = "收款金额不能为空")
    private BigDecimal receiveAmount;

    @ApiModelProperty("现金")
    @NotNull(message = "现金不能为空")
    private BigDecimal cashAmount;

    @ApiModelProperty("油卡费用")
    @NotNull(message = "油卡费用不能为空")
    private BigDecimal oilCardAmount;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("分配货物")
    @NotEmpty(message = "货物信息不能为空")
    @Valid
    private List<WayBillCreateGoodsForm> goodsFormList;

    @ApiModelProperty("分段运输信息")
    private WaybillCreateSplitTransportForm splitTransportForm;

    @ApiModelPropertyEnum(value = WaybillTransportModeEnum.class, desc = "运输方式")
    @NotNull(message = "运输方式不能为空")
    @CheckEnum(value = WaybillTransportModeEnum.class, message = "运输方式不正确")
    private Integer transportMode;

    @ApiModelProperty("业务时间")
    @NotNull(message = "业务时间不能为空")
    private LocalDate businessDate;

    @ApiModelProperty(value = "装货时间")
    @NotNull(message = "装货时间不能为空")
    private LocalDateTime loadTime;

    @ApiModelProperty(value = "卸货时间")
    @NotNull(message = "卸货时间不能为空")
    private LocalDateTime unloadTime;
}
