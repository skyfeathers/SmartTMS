package net.lab1024.tms.admin.module.business.order.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.admin.module.business.order.domain.dto.OrderImportDTO;
import net.lab1024.tms.admin.module.business.order.domain.form.OrderPathImportForm;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.module.business.order.constant.GoodsUnitTypeEnum;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单导入验证结果
 *
 * @author lidoudou
 * @date 2022/11/19 下午5:11
 */
@Data
public class OrderImportResultVO extends OrderImportDTO {

    @ApiModelProperty("货主ID")
    private Long shipperId;

    @ApiModelProperty("调度员")
    @NotNull(message = "调度员不能为空")
    private Long scheduleId;

    @ApiModelProperty("运输类型")
    @NotNull(message = "运输类型不能为空")
    private Integer businessTypeCode;

    @ApiModelProperty("运输方式")
    @NotNull(message = "运输方式不能为空")
    private Integer transportMode;

    @ApiModelProperty("业务类型")
    private Long containerBusinessTypeId;

    @ApiModelProperty("柜型")
    private Long cabinetId;

    @ApiModelProperty("货物类型")
    @NotBlank(message = "货物类型不能为空")
    private String goodsType;

    @ApiModelPropertyEnum(value = GoodsUnitTypeEnum.class, desc = "单位")
    @NotNull(message = "货物单位不能为空")
    private Integer goodsUnit;

    @ApiModelProperty("运输路线ID")
    @NotNull(message = "运输路线不能为空")
    private Long transportRouteId;

    @ApiModelProperty("运输路线ID")
    @NotNull(message = "运输路线信息不能为空")
    private List<OrderPathImportForm> pathList;

    @ApiModelProperty("创建人")
    @NotNull(message = "创建人不能为空")
    private Long createUserId;


    @ApiModelProperty("司机ID")
    private Long driverId;

    @ApiModelProperty("车辆ID")
    private Long vehicleId;

    @ApiModelProperty("信息错误提示")
    private String errorMsg;

    @ApiModelProperty("订单ID")
    private Long orderId;

    @ApiModelProperty("结算方式")
    private Integer settleMode;
}
