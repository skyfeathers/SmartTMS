package net.lab1024.tms.admin.module.business.carcost.tabulation.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.domain.PageParam;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.common.validator.enumeration.CheckEnum;
import net.lab1024.tms.common.constant.AuditStatusEnum;
import net.lab1024.tms.common.module.business.carcost.constant.CarCostCategoryCostTypeEnum;
import org.hibernate.validator.constraints.Length;

/**
 * 自有车费用列表 Form
 *
 * @author zhaoxinyang
 * @date 2023/11/02 09:57
 */
@Data
public class CarCostTabulationQueryForm extends PageParam {

    @ApiModelProperty("司机ID")
    private Long driverId;

    @ApiModelProperty("车辆ID")
    private Long vehicleId;

    @ApiModelProperty("分类ID")
    private Long categoryId;

    @ApiModelProperty("运单号")
    @Length(max = 200, message = "运单号最多200个字符")
    private String waybillNumber;

    @ApiModelProperty("箱号")
    @Length(max = 200, message = "箱号最多200个字符")
    private String containerNumber;

    @ApiModelPropertyEnum(value = AuditStatusEnum.class, desc = "审核状态")
    @CheckEnum(value = AuditStatusEnum.class, message = "审核状态错误")
    private Integer auditStatus;

    @ApiModelPropertyEnum(value = CarCostCategoryCostTypeEnum.class, desc = "费用类型")
    @CheckEnum(value = CarCostCategoryCostTypeEnum.class, message = "费用类型错误")
    private Integer costType;


    @ApiModelProperty(value = "企业id", hidden = true)
    private Long enterpriseId;
}
