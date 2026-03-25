package net.lab1024.tms.admin.module.business.bracket.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.common.validator.enumeration.CheckEnum;
import net.lab1024.tms.common.constant.AuditStatusEnum;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 挂车批量审核
 *
 * @author lidoudou
 * @date 2022/7/17 下午2:56
 */
@Data
public class BracketBatchAuditForm {

    @NotNull(message = "请选择挂车")
    @ApiModelProperty("挂车ID列表")
    private List<Long> bracketIdList;

    @ApiModelProperty("审核状态")
    @NotNull(message = "审核结果不能为空")
    @CheckEnum(value = AuditStatusEnum.class, message = "审核结果错误", required = true)
    @ApiModelPropertyEnum(value = AuditStatusEnum.class, desc = "审核状态")
    private Integer auditStatus;

    @ApiModelProperty("备注")
    private String remark;
}
