package net.lab1024.tms.admin.module.business.waybill.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.module.business.waybill.domain.form.WaybillVoucherForm;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author yandy
 * @description:
 * @date 2022/8/25 10:30 上午
 */
@Data
public class WaybillDeliverGoodsForm extends WaybillVoucherForm {

    @ApiModelProperty("运单id")
    @NotNull(message = "运单id不能为空")
    private Long waybillId;

    @ApiModelProperty("发货时间")
    @NotNull(message = "发货时间不能为空")
    private LocalDateTime driverGoodsTime;
}