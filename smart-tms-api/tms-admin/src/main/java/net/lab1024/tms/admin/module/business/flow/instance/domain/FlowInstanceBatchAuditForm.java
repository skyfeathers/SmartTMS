package net.lab1024.tms.admin.module.business.flow.instance.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.module.business.flow.FlowAuditStatusEnum;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * [  ]
 *
 * @author yandanyang
 * @date 2021/8/18 16:26
 */
@Data
public class FlowInstanceBatchAuditForm {

    @ApiModelProperty("审批实例id")
    @NotEmpty(message = "审批实例id不能为空")
    @Size(max = 20, message = "最多可批量审核20个业务单据")
    private List<Long> instanceIdList;

    @ApiModelPropertyEnum(value = FlowAuditStatusEnum.class,desc = "审核状态")
    @NotNull(message = "审核状态不能为空")
    private Integer auditStatus;

    @ApiModelProperty("审批备注")
    private String auditRemark;

    @ApiModelProperty("扩展数据-json")
    private String extData;

}
