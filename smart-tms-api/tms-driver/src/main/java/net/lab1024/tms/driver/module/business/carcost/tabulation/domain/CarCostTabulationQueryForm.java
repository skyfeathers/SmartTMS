package net.lab1024.tms.driver.module.business.carcost.tabulation.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.domain.PageParam;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.common.validator.enumeration.CheckEnum;
import net.lab1024.tms.common.constant.AuditStatusEnum;
import net.lab1024.tms.common.module.business.carcost.constant.CarCostModuleTypeEnum;
import org.hibernate.validator.constraints.Length;

/**
 * 自有车费用列表 Form
 *
 * @author zhaoxinyang
 * @date 2023/11/02 09:57
 */
@Data
public class CarCostTabulationQueryForm extends PageParam {

    @ApiModelProperty("关键字: 运单号,车牌号")
    @Length(max = 200, message = "关键字最多200个字符")
    private String keywords;

    @ApiModelPropertyEnum(value = CarCostModuleTypeEnum.class, desc = "模块类型")
    @CheckEnum(value = CarCostModuleTypeEnum.class, message = "模块类型")
    private Integer moduleType;

    @ApiModelPropertyEnum(value = AuditStatusEnum.class, desc = "审核状态")
    @CheckEnum(value = AuditStatusEnum.class, message = "审核状态错误")
    private Integer auditStatus;

    @ApiModelProperty(value = "司机ID | 司机端不需要传")
    private Long driverId;

    @ApiModelProperty(value = "创建人类型", hidden = true)
    private Integer createUserType;

}
