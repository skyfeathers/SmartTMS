package net.lab1024.tms.admin.module.business.flow.instance.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.lab1024.tms.common.module.business.flow.FlowAuditStatusEnum;

import java.time.LocalDateTime;

/**
 * [  ]
 *
 * @author yandanyang
 * @date 2021/8/16 16:59
 */
@Data
@TableName("t_flow_instance_task_record")
public class FlowInstanceTaskRecordEntity {

    @TableId(type = IdType.AUTO)
    private Long recordId;
    /**
     * 流程实例id
     */
    private Long instanceId;

    /**
     * 企业id
     */
    private Long enterpriseId;

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
