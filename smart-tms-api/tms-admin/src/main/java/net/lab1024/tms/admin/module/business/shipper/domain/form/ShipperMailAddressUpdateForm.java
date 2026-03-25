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
public class ShipperMailAddressUpdateForm extends ShipperMailAddressAddForm{

    @ApiModelProperty("mailAddressId")
    @NotNull(message = "mailAddressId不能为空")
    private Long mailAddressId;

}