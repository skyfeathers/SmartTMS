package net.lab1024.tms.admin.module.business.flow.instance.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.admin.module.business.flow.comment.FlowCommentVO;
import net.lab1024.tms.admin.module.business.flow.constant.FlowTaskTypeEnum;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.module.business.flow.FlowAuditStatusEnum;

import java.time.LocalDateTime;
import java.util.List;

/**
 * [  ]
 *
 * @author yandanyang
 * @date 2021/8/18 10:07
 */
@Data
public class FlowInstanceTaskVO {

    @ApiModelProperty("id")
    private Long instanceTaskId;

    @ApiModelProperty("流程实例id")
    private Long instanceId;

    @ApiModelProperty("审批流id")
    private Long flowId;

    @ApiModelProperty("任务节点id")
    private Long taskId;

    @ApiModelPropertyEnum(value = FlowTaskTypeEnum.class,desc = "任务类型")
    private Integer taskType;

    @ApiModelProperty("任务名称")
    private String taskName;

    @ApiModelProperty("处理任务的用户名称")
    private String handlerNames;

    @ApiModelProperty("处理任务的用户id")
    private String handlers;

    @ApiModelProperty("操作代码")
    private String operateCode;

    @ApiModelProperty("是否已处理")
    private Boolean handleFlag;

    @ApiModelProperty("审核记录处理人名称")
    private String auditRecordHandlerNames;

    @ApiModelPropertyEnum(value = FlowAuditStatusEnum.class,desc = "审核状态")
    private Integer auditStatus;

    @ApiModelProperty("审核备注")
    private String auditRemark;

    @ApiModelProperty("审批时间")
    private LocalDateTime finishTime;

    @ApiModelProperty("审批评论")
    private List<FlowCommentVO> commentList;
}
