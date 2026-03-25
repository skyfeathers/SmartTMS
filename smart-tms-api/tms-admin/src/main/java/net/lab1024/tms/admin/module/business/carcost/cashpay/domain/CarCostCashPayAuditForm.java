package net.lab1024.tms.admin.module.business.carcost.cashpay.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.common.validator.enumeration.CheckEnum;
import net.lab1024.tms.common.constant.AuditStatusEnum;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * 现金支出 Audit Form
 *
 * @author zhaoxinyang
 * @date 2023/10/26 16:07
 */
@Data
public class CarCostCashPayAuditForm {

    @ApiModelProperty("现金支出ID")
    @NotNull(message = "现金支出ID不能为空")
    private Long cashPayId;

    @NotNull(message = "审核状态不能为空")
    @ApiModelPropertyEnum(value = AuditStatusEnum.class, desc = "审核状态")
    @CheckEnum(value = AuditStatusEnum.class, message = "审核状态")
    private Integer auditStatus;

    @ApiModelProperty("审核备注")
    @Length(max = 100, message = "审核备注最多100字符")
    private String auditRemark;

}
