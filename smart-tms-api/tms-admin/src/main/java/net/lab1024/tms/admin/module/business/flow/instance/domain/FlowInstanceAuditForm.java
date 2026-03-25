package net.lab1024.tms.admin.module.business.flow.instance.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.module.business.flow.FlowAuditStatusEnum;

import javax.validation.constraints.NotNull;

/**
 * [  ]
 *
 * @author yandanyang
 * @date 2021/8/18 16:26
 */
@Data
public class FlowInstanceAuditForm {

    @ApiModelProperty("审批实例id")
    @NotNull(message = "审批实例id不能为空")
    private Long instanceId;

    @ApiModelPropertyEnum(value = FlowAuditStatusEnum.class,desc = "审核状态")
    @NotNull(message = "审核状态不能为空")
    private Integer auditStatus;

    @ApiModelProperty("审批备注")
    private String auditRemark;

    @ApiModelProperty("扩展数据-json")
    private String extData;

}
