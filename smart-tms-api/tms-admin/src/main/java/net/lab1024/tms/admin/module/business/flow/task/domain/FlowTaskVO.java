package net.lab1024.tms.admin.module.business.flow.task.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.admin.module.business.flow.constant.FlowTaskTypeEnum;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;

import java.time.LocalDateTime;

/**
 * 审批流程任务
 *
 * @author zhaoxinyang
 * @date 2024/9/25 11:22
 */
@Data
public class FlowTaskVO {

    @ApiModelProperty("任务ID")
    private Long taskId;

    @ApiModelProperty("审批流程ID")
    private Long flowId;

    @ApiModelProperty("任务名称")
    private String taskName;

    @ApiModelPropertyEnum(value = FlowTaskTypeEnum.class, desc = "任务类型")
    private Integer taskType;

    @ApiModelProperty("任务配置")
    private String taskConfig;

    @ApiModelProperty("任务监听")
    private String taskListener;

    @ApiModelProperty("操作代码")
    private String operateCode;

    @ApiModelProperty("父级任务ids | 开始任务父级任务ids为0")
    private String parentIds;

    @ApiModelProperty("说明")
    private String taskDesc;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

}
