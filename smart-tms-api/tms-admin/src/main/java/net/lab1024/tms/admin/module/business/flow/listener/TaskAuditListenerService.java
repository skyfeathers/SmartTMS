package net.lab1024.tms.admin.module.business.flow.listener;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 * @author yandy
 * @description:
 * @date 2022/3/16 1:57 下午
 */
@Slf4j
@Service
public class TaskAuditListenerService {

    @Autowired
    private ApplicationContext applicationContext;

    /**
     * 获取任务审核监听器
     *
     * @param taskListener
     * @return
     */
    private ITaskAuditListenerService getListener(String taskListener) {
        if (StringUtils.isBlank(taskListener)) {
            return null;
        }
        ITaskAuditListenerService taskAuditListenerService = applicationContext.getBean(taskListener, ITaskAuditListenerService.class);
        if (taskAuditListenerService == null) {
            log.warn("[taskAuditListenerService] service name {} cannot found!", taskListener);
            return null;
        }
        return taskAuditListenerService;
    }

    /**
     * 触发审核通过的任务
     *
     * @param taskAuditListenerBO
     */
    public void triggerPass(TaskAuditListenerBO taskAuditListenerBO) {
        ITaskAuditListenerService taskAuditListenerService = this.getListener(taskAuditListenerBO.getTaskListener());
        if (taskAuditListenerService == null) {
            return;
        }
        taskAuditListenerService.auditPass(taskAuditListenerBO);
    }

    /**
     * 触发审核驳回的任务
     *
     * @param taskAuditListenerBO
     */
    public void triggerReject(TaskAuditListenerBO taskAuditListenerBO) {
        ITaskAuditListenerService taskAuditListenerService = this.getListener(taskAuditListenerBO.getTaskListener());
        if (taskAuditListenerService == null) {
            return;
        }
        taskAuditListenerService.auditReject(taskAuditListenerBO);
    }
}