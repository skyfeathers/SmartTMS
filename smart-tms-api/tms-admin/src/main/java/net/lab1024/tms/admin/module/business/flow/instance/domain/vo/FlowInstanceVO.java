package net.lab1024.tms.admin.module.business.flow.instance.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.admin.module.business.flow.constant.FlowTypeEnum;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.module.business.flow.FlowAuditStatusEnum;

import java.time.LocalDateTime;

/**
 * [  ]
 *
 * @author yandanyang
 * @date 2021/8/18 14:51
 */
@Data
public class FlowInstanceVO {

    @ApiModelProperty("流程实例id")
    private Long instanceId;

    @ApiModelProperty("审批流id")
    private Long flowId;

    @ApiModelPropertyEnum(value = FlowTypeEnum.class, desc = "流程类型")
    private Integer flowType;

    @ApiModelPropertyEnum(value = FlowAuditStatusEnum.class, desc = "审核状态")
    private Integer auditStatus;

    @ApiModelProperty("发起人id")
    private Long initiatorId;

    @ApiModelProperty("发起人id")
    private String initiatorName;

    @ApiModelProperty("当前任务节点id")
    private Long currentTaskId;

    @ApiModelProperty("当前任务节点id")
    private String currentTaskName;

    @ApiModelProperty("当前任务处理人名称")
    private String currentHandlerNames;

    @ApiModelProperty("当前任务的操作代码")
    private String currentOperateCode;

    @ApiModelProperty("业务id")
    private Long businessId;

    @ApiModelProperty("业务编码")
    private String businessCode;

    @ApiModelProperty("业务数据")
    private String businessData;

    @ApiModelProperty("扩展字段1")
    private String extendField1;
    @ApiModelProperty("扩展字段1名称")
    private String extendFieldName1;

    @ApiModelProperty("扩展字段2")
    private String extendField2;
    @ApiModelProperty("扩展字段2名称")
    private String extendFieldName2;

    @ApiModelProperty("扩展字段3")
    private String extendField3;
    @ApiModelProperty("扩展字段3名称")
    private String extendFieldName3;

    @ApiModelProperty("扩展字段4")
    private String extendField4;
    @ApiModelProperty("扩展字段4名称")
    private String extendFieldName4;

    @ApiModelProperty("扩展字段5")
    private String extendField5;
    @ApiModelProperty("扩展字段5名称")
    private String extendFieldName5;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("审批结束时间")
    private LocalDateTime finishTime;

    @ApiModelProperty("发起审批时间")
    private LocalDateTime createTime;

}
