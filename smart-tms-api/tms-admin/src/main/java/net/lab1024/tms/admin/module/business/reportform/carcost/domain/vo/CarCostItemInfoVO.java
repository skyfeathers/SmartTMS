package net.lab1024.tms.admin.module.business.reportform.carcost.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.constant.AuditStatusEnum;
import net.lab1024.tms.common.module.business.carcost.constant.CarCostCategoryCostTypeEnum;
import net.lab1024.tms.common.module.business.carcost.constant.CarCostCategoryPayModeEnum;
import net.lab1024.tms.common.module.business.carcost.constant.CarCostModuleTypeEnum;

import java.math.BigDecimal;

/**
 * 自有车-费用信息 VO
 *
 * @author zhaoxinyang
 * @date 2023/10/23 16:04
 */
@Data
public class CarCostItemInfoVO {

    @ApiModelProperty
    private Long waybillId;

    @ApiModelProperty("模块ID")
    private Long moduleId;

    @ApiModelPropertyEnum(value = CarCostModuleTypeEnum.class, desc = "模块类型")
    private Integer moduleType;

    @ApiModelProperty("金额")
    private BigDecimal amount;

    @ApiModelPropertyEnum(value = AuditStatusEnum.class, desc = "审核状态")
    private Integer auditStatus;

    @ApiModelProperty("分类ID")
    private Long categoryId;

    @ApiModelPropertyEnum(value = CarCostCategoryCostTypeEnum.class, desc = "费用类型")
    private Integer costType;

    @ApiModelProperty("分类名字")
    private String categoryName;

    @ApiModelPropertyEnum(value = CarCostCategoryPayModeEnum.class, desc = "支付方式")
    private Integer payMode;

    @ApiModelProperty("油卡ID")
    private Long oilCardId;

    @ApiModelProperty("备注")
    private String remark;
}
