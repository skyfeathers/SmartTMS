package net.lab1024.tms.admin.module.business.waybill.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.validator.enumeration.CheckEnum;
import net.lab1024.tms.common.module.business.waybill.constant.WaybillTransportModeEnum;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author yandy
 * @description:
 * @date 2022/8/12 4:42 下午
 */
@Data
public class WaybillUpdateForm extends WaybillSettleTypeForm {

    @ApiModelProperty("运单id")
    @NotNull(message = "运单id不能为空")
    private Long waybillId;

    @NotNull(message = "业务类型不能为空")
    @CheckEnum(value = WaybillTransportModeEnum.class, message = "业务类型不正确")
    private Integer transportMode;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty(value = "业务时间",example = "2023-08-01")
    @NotNull(message = "业务时间不能为空")
    private LocalDate businessDate;

    @ApiModelProperty("装货时间")
    @NotNull(message = "装货时间不能为空")
    private LocalDateTime loadTime;

    @ApiModelProperty("卸货时间")
    @NotNull(message = "卸货时间不能为空")
    private LocalDateTime unloadTime;

}