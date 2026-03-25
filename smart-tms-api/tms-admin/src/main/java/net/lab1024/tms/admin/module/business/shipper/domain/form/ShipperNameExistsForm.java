package net.lab1024.tms.admin.module.business.shipper.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 判断货主名称是否存在
 *
 * @author lidoudou
 * @date 2022/11/14 下午4:29
 */
@Data
public class ShipperNameExistsForm {

    @ApiModelProperty("货主ID")
    private Long shipperId;

    @ApiModelProperty("货主名称")
    @NotEmpty(message = "货主名称不能为空")
    private String consignor;

    @ApiModelProperty(value = "所属公司",hidden = true)
    private Long enterpriseId;
}
