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
@TableName("t_flow_instance_task")
public class FlowInstanceTaskEntity {

    @TableId(type = IdType.AUTO)
    private Long instanceTaskId;

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

    private Integer taskType;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 任务监听
     */
    private String taskListener;
    /**
     * 操作代码，可根据此字段判断进行什么样的操作
     */
    private String operateCode;

    /**
     * 处理此节点的用户id集合
     */
    private String handlers;
    /**
     * 处理此节点的用户名称集合
     */
    private String handlerNames;
    /**
     * 是否已处理
     */
    private Boolean handleFlag;

    /**
     * 审核状态
     * {@link FlowAuditStatusEnum}
     */
    private Integer auditStatus;
    /**
     * 审核备注
     */
    private String auditRemark;

    /**
     * 审批结束时间
     */
    private LocalDateTime finishTime;

    private LocalDateTime updateTime;

    private LocalDateTime createTime;
}
