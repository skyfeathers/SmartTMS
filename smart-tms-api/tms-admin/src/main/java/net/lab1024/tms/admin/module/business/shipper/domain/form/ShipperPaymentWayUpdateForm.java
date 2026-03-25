package net.lab1024.tms.admin.module.business.shipper.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 *
 * @author yandy
 * @description:
 * @date 2026/1/22 上午10:49
 */
@Data
public class ShipperPaymentWayUpdateForm extends ShipperPaymentWayAddForm {

    @ApiModelProperty("paymentWayId")
    @NotNull(message = "paymentWayId不能为空")
    private Long paymentWayId;


}