package net.lab1024.tms.admin.module.business.carcost.cashreceive.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.common.validator.enumeration.CheckEnum;
import net.lab1024.tms.common.constant.AuditStatusEnum;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * 现金收入 Audit Form
 *
 * @author zhaoxinyang
 * @date 2023/10/26 16:07
 */
@Data
public class CarCostCashReceiveAuditForm {

    @ApiModelProperty("现金收入ID")
    @NotNull(message = "现金收入ID不能为空")
    private Long cashReceiveId;

    @ApiModelPropertyEnum(value = AuditStatusEnum.class, desc = "审核状态")
    @CheckEnum(value = AuditStatusEnum.class, message = "审核状态错误", required = true)
    private Integer auditStatus;

    @ApiModelProperty("审核备注")
    @Length(max = 100, message = "审核备注最多100字符")
    private String auditRemark;

}
