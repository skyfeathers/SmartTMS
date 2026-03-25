package net.lab1024.tms.admin.module.business.flow.msg;

import net.lab1024.tms.admin.module.business.flow.comment.FlowCommentEntity;
import net.lab1024.tms.admin.module.business.flow.instance.domain.entity.FlowInstanceEntity;
import net.lab1024.tms.common.module.business.flow.FlowAuditStatusEnum;

import java.util.List;

/**
 * [ 各审批类型对应的消息体 ]
 *
 * @author yandanyang
 * @date 2021/10/15 15:24
 */
public abstract class BaseFlowMsgBuildHandleService<T> implements IFlowMsgBuildHandleService {

    /**
     * 流程类型
     *
     * @return
     */
    @Override
    public abstract List<Integer> flowType();

    /**
     * 审核提醒 -》给下一任务处理人
     *
     * @param flowInstanceEntity
     * @return
     */
    @Override
    public abstract T auditRemindMsg(FlowInstanceEntity flowInstanceEntity);


    /**
     * 审核通知 -》给申请人
     *
     * @param flowInstanceEntity
     * @param auditorId
     * @param auditStatusEnum
     * @return
     */
    @Override
    public abstract T auditResultMsg(FlowInstanceEntity flowInstanceEntity, Long auditorId, FlowAuditStatusEnum auditStatusEnum);


    /**
     * 发送评论给接收人
     * @param flowInstanceEntity
     * @param flowCommentEntity
     * @param operatorName
     */
    @Override
    public abstract T sendAuditCommentMsg(FlowInstanceEntity flowInstanceEntity, FlowCommentEntity flowCommentEntity, String operatorName);
}
