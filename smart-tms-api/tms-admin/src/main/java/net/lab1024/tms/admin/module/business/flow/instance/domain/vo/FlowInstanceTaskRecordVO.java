package net.lab1024.tms.admin.module.business.flow.instance.domain.vo;

import lombok.Data;
import net.lab1024.tms.common.module.business.flow.FlowAuditStatusEnum;

import java.time.LocalDateTime;

/**
 * @author yandy
 * @description:
 * @date 2022/10/27 6:10 下午
 */
@Data
public class FlowInstanceTaskRecordVO {

    private Long recordId;
    /**
     * 流程实例id
     */
    private Long instanceId;

    /**
     * 审批流id
     */
    private Long flowId;

    /**
     * 任务节点（只包含审批节点和抄送节点）
     */
    private Long taskId;

    /**
     * 处理人
     */
    private Long handler;

    /**
     * 处理人
     */
    private String handlerName;

    /**
     * 审核状态
     * {@link FlowAuditStatusEnum}
     */
    private Integer auditStatus;

    /**
     * 审核备注
     */
    private String auditRemark;

    private LocalDateTime updateTime;

    private LocalDateTime createTime;
}