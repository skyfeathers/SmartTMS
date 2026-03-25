package net.lab1024.tms.admin.module.business.carcost.tabulation.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.common.validator.enumeration.CheckEnum;
import net.lab1024.tms.common.constant.AuditStatusEnum;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * 费用列表的审核
 *
 * @author zhaoxinyang
 * @date 2023/11/08 10:47
 */
@Data
public class CarCostTabulationAuditForm {

    @ApiModelProperty("费用列表ID")
    @NotNull(message = "费用列表ID不能为空")
    private Long tabulationId;

    @ApiModelPropertyEnum(value = AuditStatusEnum.class, desc = "审核状态")
    @NotNull(message = "审核状态不能为空")
    @CheckEnum(value = AuditStatusEnum.class, message = "审核状态错误")
    private Integer auditStatus;

    @ApiModelProperty("审核备注")
    @Length(max = 200, message = "审核备注最多200个字符")
    private String auditRemark;

}
