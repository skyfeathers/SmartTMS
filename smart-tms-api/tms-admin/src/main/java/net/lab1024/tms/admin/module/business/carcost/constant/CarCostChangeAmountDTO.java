package net.lab1024.tms.admin.module.business.carcost.constant;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.module.business.carcost.constant.CarCostCategoryPayModeEnum;

import java.math.BigDecimal;

/**
 * 变动金额 DTO
 *
 * @author zhaoxinyang
 * @date 2023/10/25 15:50
 */
@Data
@AllArgsConstructor
public class CarCostChangeAmountDTO {

    @ApiModelProperty("司机ID | 油卡ID")
    private Long moduleId;

    @ApiModelProperty("主卡ID")
    private Long masterCardId;

    @ApiModelProperty("变动金额 可能为负数 使用时用余额加上变动金额")
    private BigDecimal changeAmount;

    @ApiModelPropertyEnum(value = CarCostCategoryPayModeEnum.class, desc = "支付方式")
    private Integer payMode;

    public CarCostChangeAmountDTO(Long moduleId, BigDecimal changeAmount, Integer payMode) {
        this.moduleId = moduleId;
        this.changeAmount = changeAmount;
        this.payMode = payMode;
    }
}
