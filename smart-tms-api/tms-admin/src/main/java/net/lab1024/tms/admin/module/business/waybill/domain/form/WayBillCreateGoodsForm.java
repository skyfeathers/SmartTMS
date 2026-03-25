package net.lab1024.tms.admin.module.business.waybill.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.common.validator.enumeration.CheckEnum;
import net.lab1024.tms.common.module.business.order.constant.GoodsUnitTypeEnum;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author zhuoda
 * @Date 2022-07-13
 */
@Data
public class WayBillCreateGoodsForm {

    @ApiModelProperty("运单货物项目")
    @NotNull(message = "运单货物不能为空")
    private Long orderGoodsId;

    @ApiModelProperty("货物量")
    @NotNull(message = "货物量不能为空")
    private BigDecimal goodsQuantity;

    @ApiModelProperty("备注")
    private String remark;
}
