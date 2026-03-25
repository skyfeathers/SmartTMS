package net.lab1024.tms.admin.module.business.fleet.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.common.validator.enumeration.CheckEnum;
import net.lab1024.tms.common.constant.AuditStatusEnum;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 车队审核
 *
 * @author lihaifan
 * @date 2022/7/11 17:17
 */
@Data
public class FleetAuditForm {

    @NotNull(message = "请选择车队")
    @ApiModelProperty("车队ID")
    private List<Long> fleetIdList;

    @ApiModelProperty("审核状态")
    @NotNull(message = "审核结果不能为空")
    @CheckEnum(value = AuditStatusEnum.class, message = "审核结果错误", required = true)
    @ApiModelPropertyEnum(value = AuditStatusEnum.class, desc = "审核状态")
    private Integer auditStatus;

    @ApiModelProperty("备注")
    private String remark;
}
