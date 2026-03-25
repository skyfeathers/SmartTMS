package net.lab1024.tms.admin.module.business.flow.task.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.admin.module.business.flow.constant.FlowTaskTypeEnum;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.common.validator.enumeration.CheckEnum;

import javax.validation.constraints.NotBlank;

/**
 * 新建 审批流程任务
 *
 * @author zhaoxinyang
 * @date 2024/9/25 10:10
 */
@Data
public class FlowTaskCreateForm {

    @ApiModelProperty("任务名称")
    @NotBlank(message = "任务名称不能为空")
    private String taskName;

    @ApiModelPropertyEnum(value = FlowTaskTypeEnum.class, desc = "任务类型")
    @CheckEnum(value = FlowTaskTypeEnum.class, message = "任务类型错误", required = true)
    private Integer taskType;

    @ApiModelProperty("任务配置")
    @NotBlank(message = "任务配置不能为空")
    private String taskConfig;

    @ApiModelProperty("任务监听")
    private String taskListener;

    @ApiModelProperty("操作代码")
    private String operateCode;

    @ApiModelProperty("说明")
    private String taskDesc;

}
