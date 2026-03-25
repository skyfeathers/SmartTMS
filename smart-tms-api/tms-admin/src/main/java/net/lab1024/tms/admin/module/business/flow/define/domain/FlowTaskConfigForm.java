package net.lab1024.tms.admin.module.business.flow.define.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.admin.module.business.flow.constant.FlowTypeEnum;
import net.lab1024.tms.admin.module.business.flow.task.domain.FlowTaskCreateForm;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.common.validator.enumeration.CheckEnum;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 新建 审批流程
 *
 * @author zhaoxinyang
 * @date 2024/9/25 9:29
 */
@Data
public class FlowTaskConfigForm {
    @ApiModelProperty("流程id")
    @NotNull(message = "流程id不能为空")
    private Long flowId;

    @Valid
    @ApiModelProperty("流程任务列表")
    private List<FlowTaskCreateForm> taskList;

    @ApiModelProperty(hidden = true)
    @NotNull(message = "企业id不能为空")
    private Long enterpriseId;
}
