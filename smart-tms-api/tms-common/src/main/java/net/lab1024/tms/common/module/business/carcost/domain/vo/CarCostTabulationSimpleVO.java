package net.lab1024.tms.common.module.business.carcost.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.constant.AuditStatusEnum;
import net.lab1024.tms.common.module.business.carcost.constant.CarCostModuleTypeEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 自有车列表 简单VO
 *
 * @author zhaoxinyang
 * @date 2023/11/02 09:57
 */
@Data
public class CarCostTabulationSimpleVO {

    @ApiModelProperty("自有车费用列表ID")
    private Long tabulationId;

    @ApiModelProperty("模块ID")
    private Long moduleId;

    @ApiModelPropertyEnum(value = CarCostModuleTypeEnum.class, desc = "模块类型")
    private Integer moduleType;

    @ApiModelProperty("金额")
    private BigDecimal amount;

    @ApiModelPropertyEnum(value = AuditStatusEnum.class, desc = "审核状态")
    private Integer auditStatus;

    @ApiModelProperty("审核备注")
    private String auditRemark;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

}
