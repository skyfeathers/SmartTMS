package net.lab1024.tms.admin.module.business.flow.listener;

/**
 * @author yandy
 * @description:
 * @date 2022/3/16 11:51 上午
 */
public interface ITaskAuditListenerService {

    /**
     * 任务审核通过
     * 注意 如果是最后一个阶段，此触发器照样会执行,先执行任务监听，再触发流程监听
     * @param taskAuditListenerBO
     */
    void auditPass(TaskAuditListenerBO taskAuditListenerBO);

    /**
     * 任务审核驳回
     * 注意 如果是最后一个阶段，此触发器照样会执行,先执行任务监听，再触发流程监听
     * @param taskAuditListenerBO
     */
    void auditReject(TaskAuditListenerBO taskAuditListenerBO);
}