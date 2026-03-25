package net.lab1024.tms.admin.module.business.flow.define.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.admin.module.business.flow.constant.FlowTypeEnum;
import net.lab1024.tms.admin.module.business.flow.task.domain.FlowTaskVO;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 审批流程
 *
 * @author zhaoxinyang
 * @date 2024/9/25 11:20
 */
@Data
public class FlowVO {

    @ApiModelProperty("审批流程ID")
    private Long flowId;

    @ApiModelPropertyEnum(value = FlowTypeEnum.class, desc = "流程类型")
    private Integer flowType;

    @ApiModelProperty("流程名称")
    private String flowName;

    @ApiModelProperty("流程说明")
    private String flowDesc;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("审批流程任务列表")
    private List<FlowTaskVO> taskList;

}
