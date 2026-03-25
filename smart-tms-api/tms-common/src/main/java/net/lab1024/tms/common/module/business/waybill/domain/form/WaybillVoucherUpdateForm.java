package net.lab1024.tms.common.module.business.waybill.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author zhuoda
 * @Date 2022-07-13
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WaybillVoucherUpdateForm extends WaybillVoucherForm{

    @ApiModelProperty("凭证id")
    @NotNull(message = "运单不能为空")
    private Long waybillVoucherId;


}
