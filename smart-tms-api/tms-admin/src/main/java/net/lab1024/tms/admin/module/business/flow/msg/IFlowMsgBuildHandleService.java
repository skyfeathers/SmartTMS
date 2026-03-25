package net.lab1024.tms.admin.module.business.flow.msg;


import net.lab1024.tms.admin.module.business.flow.comment.FlowCommentEntity;
import net.lab1024.tms.admin.module.business.flow.instance.domain.entity.FlowInstanceEntity;
import net.lab1024.tms.common.module.business.flow.FlowAuditStatusEnum;

import java.util.List;

/**
 * [  ]
 *
 * @author yandanyang
 * @date 2021/10/15 15:24
 */
public interface IFlowMsgBuildHandleService<T> {

    /**
     * 流程类型
     *
     * @return
     */
    List<Integer> flowType();

    /**
     * 审核提醒 -》给下一任务处理人
     *
     * @param flowInstanceEntity
     * @return
     */
    T auditRemindMsg(FlowInstanceEntity flowInstanceEntity);


    /**
     * 审核通知 -》给申请人
     *
     * @param flowInstanceEntity
     * @param auditorId
     * @param auditStatusEnum
     * @return
     */
    T auditResultMsg(FlowInstanceEntity flowInstanceEntity, Long auditorId, FlowAuditStatusEnum auditStatusEnum);

    /**
     * 流程评论 -》给途径的审批节点
     * @param flowInstanceEntity
     * @param flowCommentEntity
     * @param operatorName
     * @return
     */
    T sendAuditCommentMsg(FlowInstanceEntity flowInstanceEntity, FlowCommentEntity flowCommentEntity, String operatorName);

}
