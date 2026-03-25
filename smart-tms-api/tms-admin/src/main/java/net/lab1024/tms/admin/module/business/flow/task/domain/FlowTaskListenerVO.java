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
public class FlowTaskListenerVO {

    @ApiModelProperty("监听器beanName")
    private String name;

    @ApiModelProperty("描述")
    private String desc;


}
