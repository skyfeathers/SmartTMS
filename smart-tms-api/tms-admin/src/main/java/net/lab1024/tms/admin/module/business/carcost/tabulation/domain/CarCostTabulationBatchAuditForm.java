package net.lab1024.tms.admin.module.business.carcost.tabulation.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.common.validator.enumeration.CheckEnum;
import net.lab1024.tms.common.constant.AuditStatusEnum;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 自有车费用批量审核
 *
 * @author zhaoxinyang
 * @date 2024/9/27 11:06
 */
@Data
public class CarCostTabulationBatchAuditForm {

    @ApiModelProperty("费用ID列表")
    @Size(min = 1, message = "费用ID列表不能为空")
    @NotEmpty(message = "费用ID列表不能为空")
    private List<Long> tabulationIdList;

    @ApiModelPropertyEnum(value = AuditStatusEnum.class, desc = "审核状态")
    @NotNull(message = "审核状态不能为空")
    @CheckEnum(value = AuditStatusEnum.class, message = "审核状态错误")
    private Integer auditStatus;

    @ApiModelProperty("审核备注")
    @Length(max = 200, message = "审核备注最多200个字符")
    private String auditRemark;

}
