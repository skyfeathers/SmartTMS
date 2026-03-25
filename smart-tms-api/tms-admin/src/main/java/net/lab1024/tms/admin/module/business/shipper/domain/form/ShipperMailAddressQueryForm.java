package net.lab1024.tms.admin.module.business.shipper.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.domain.PageParam;

import javax.validation.constraints.NotNull;

/**
 * [  ]
 *
 * @author zhuoda
 * @date 2020/8/31 16:30
 */
@Data
public class ShipperMailAddressQueryForm extends PageParam {

    @ApiModelProperty("货主id")
    @NotNull(message = "货主id不能为空")
    private Long shipperId;

    @ApiModelProperty("是否删除")
    private Boolean deletedFlag;

}
