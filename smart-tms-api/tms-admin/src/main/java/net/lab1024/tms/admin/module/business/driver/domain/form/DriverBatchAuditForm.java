package net.lab1024.tms.admin.module.business.driver.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.common.validator.enumeration.CheckEnum;
import net.lab1024.tms.common.constant.AuditStatusEnum;

import javax.validation.constraints.NotNull;
import java.util.List;

/***
 * 司机批量审核
 *
 * @author lidoudou
 * @date 2022/6/21 上午11:06
 */
@Data
public class DriverBatchAuditForm {

    @NotNull(message = "请选择司机")
    @ApiModelProperty("司机ID")
    private List<Long> driverIdList;

    @ApiModelProperty("审核状态")
    @NotNull(message = "审核结果不能为空")
    @CheckEnum(value = AuditStatusEnum.class, message = "审核结果错误", required = true)
    @ApiModelPropertyEnum(value = AuditStatusEnum.class, desc = "审核状态")
    private Integer auditStatus;

    @ApiModelProperty("备注")
    private String remark;
}
