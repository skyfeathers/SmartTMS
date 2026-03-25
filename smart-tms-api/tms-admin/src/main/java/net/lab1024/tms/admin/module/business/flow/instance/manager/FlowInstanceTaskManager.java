package net.lab1024.tms.admin.module.business.flow.instance.manager;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.tms.admin.module.business.flow.bussiness.FlowBusinessService;
import net.lab1024.tms.admin.module.business.flow.bussiness.domain.FlowBusinessEndBO;
import net.lab1024.tms.admin.module.business.flow.constant.FlowTypeEnum;
import net.lab1024.tms.admin.module.business.flow.instance.dao.FlowInstanceDao;
import net.lab1024.tms.admin.module.business.flow.instance.dao.FlowInstanceTaskDao;
import net.lab1024.tms.admin.module.business.flow.instance.domain.entity.FlowInstanceEntity;
import net.lab1024.tms.admin.module.business.flow.instance.domain.entity.FlowInstanceTaskEntity;
import net.lab1024.tms.admin.module.business.flow.instance.domain.entity.FlowInstanceTaskRecordEntity;
import net.lab1024.tms.admin.module.business.flow.listener.TaskAuditListenerBO;
import net.lab1024.tms.admin.module.business.flow.listener.TaskAuditListenerService;
import net.lab1024.tms.admin.module.business.flow.waithandle.FlowWaitHandleService;
import net.lab1024.tms.common.common.util.SmartBaseEnumUtil;
import net.lab1024.tms.common.module.business.flow.FlowAuditStatusEnum;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * [  ]
 *
 * @author yandanyang
 * @date 2021/8/17 17:52
 */
@Slf4j
@Service
public class FlowInstanceTaskManager extends ServiceImpl<FlowInstanceTaskDao, FlowInstanceTaskEntity> {

    @Autowired
    private FlowInstanceDao flowInstanceDao;
    @Autowired
    private FlowInstanceTaskRecordManager flowInstanceTaskRecordManager;
    @Autowired
    private FlowBusinessService flowBusinessService;
    @Autowired
    private FlowWaitHandleService flowWaitHandleService;
    @Autowired
    private TaskAuditListenerService taskAuditListenerService;

    /**
     * 任务节点审核通过
     *
     * @param flowInstanceEntity
     * @param updateInstanceTaskList
     * @param insertTaskRecordList
     * @param nextTask
     * @param flowEndFlag
     */
    @Transactional(rollbackFor = Throwable.class)
    public void instanceTaskPass(FlowInstanceEntity flowInstanceEntity,
                                 List<FlowInstanceTaskEntity> updateInstanceTaskList,
                                 List<FlowInstanceTaskRecordEntity> insertTaskRecordList,
                                 FlowInstanceTaskEntity nextTask,
                                 List<TaskAuditListenerBO> taskAuditListenerList,
                                 Boolean flowEndFlag,
                                 DataTracerRequestForm dataTracerRequestForm) {
        this.updateBatchById(updateInstanceTaskList);
        if (CollectionUtils.isNotEmpty(insertTaskRecordList)) {
            flowInstanceTaskRecordManager.saveBatch(insertTaskRecordList);
        }
        //更新下一 任务处理节点
        if (nextTask != null) {
            FlowInstanceEntity updateInstanceEntity = new FlowInstanceEntity();
            updateInstanceEntity.setInstanceId(flowInstanceEntity.getInstanceId());
            updateInstanceEntity.setCurrentTaskId(nextTask.getTaskId());
            updateInstanceEntity.setCurrentTaskName(nextTask.getTaskName());
            updateInstanceEntity.setCurrentHandlers(nextTask.getHandlers());
            updateInstanceEntity.setCurrentHandlerNames(nextTask.getHandlerNames());
            updateInstanceEntity.setCurrentOperateCode(nextTask.getOperateCode());
            flowInstanceDao.updateById(updateInstanceEntity);
        }
        //如果流程已结束更新实例状态
        if (flowEndFlag) {
            FlowInstanceEntity updateInstanceEntity = new FlowInstanceEntity();
            updateInstanceEntity.setInstanceId(flowInstanceEntity.getInstanceId());
            updateInstanceEntity.setAuditStatus(FlowAuditStatusEnum.PASS.getValue());
            updateInstanceEntity.setFinishTime(LocalDateTime.now());
            flowInstanceDao.updateById(updateInstanceEntity);
        }
        //触发任务监听器
        if(CollectionUtils.isNotEmpty(taskAuditListenerList)){
            taskAuditListenerList.forEach(e->taskAuditListenerService.triggerPass(e));
        }
        //如果流程结束触发结束流程
        if (flowEndFlag) {
            FlowTypeEnum flowTypeEnum = SmartBaseEnumUtil.getEnumByValue(flowInstanceEntity.getFlowType(), FlowTypeEnum.class);
            FlowBusinessEndBO endBO = new FlowBusinessEndBO();
            endBO.setBusinessId(flowInstanceEntity.getBusinessId());
            endBO.setBusinessCode(flowInstanceEntity.getBusinessCode());
            endBO.setAuditStatusEnum(FlowAuditStatusEnum.PASS);
            flowBusinessService.triggerEnd(flowTypeEnum, endBO, dataTracerRequestForm);
        }
    }

    /**
     * 审批驳回
     *
     * @param flowInstanceEntity
     * @param updateFlowInstanceTask
     * @param instanceTaskRecordEntity
     */
    @Transactional(rollbackFor = Throwable.class)
    public void instanceTaskReject(FlowInstanceEntity flowInstanceEntity,
                                   FlowInstanceTaskEntity updateFlowInstanceTask,
                                   FlowInstanceTaskRecordEntity instanceTaskRecordEntity,
                                   TaskAuditListenerBO taskAuditListenerBO,
                                   DataTracerRequestForm dataTracerRequestForm) {
        this.updateById(updateFlowInstanceTask);
        flowInstanceTaskRecordManager.save(instanceTaskRecordEntity);
        //审核驳回后流程自动接收
        FlowInstanceEntity updateInstanceEntity = new FlowInstanceEntity();
        updateInstanceEntity.setInstanceId(flowInstanceEntity.getInstanceId());
        updateInstanceEntity.setAuditStatus(FlowAuditStatusEnum.REJECT.getValue());
        updateInstanceEntity.setFinishTime(LocalDateTime.now());
        flowInstanceDao.updateById(updateInstanceEntity);
        //触发任务监听器
        taskAuditListenerService.triggerReject(taskAuditListenerBO);
        //触发流程结束
        FlowTypeEnum flowTypeEnum = SmartBaseEnumUtil.getEnumByValue(flowInstanceEntity.getFlowType(), FlowTypeEnum.class);

        FlowBusinessEndBO endBO = new FlowBusinessEndBO();
        endBO.setBusinessId(flowInstanceEntity.getBusinessId());
        endBO.setBusinessCode(flowInstanceEntity.getBusinessCode());
        endBO.setAuditStatusEnum(FlowAuditStatusEnum.REJECT);
        flowBusinessService.triggerEnd(flowTypeEnum, endBO,dataTracerRequestForm);
        //增加待办事项
        flowWaitHandleService.increase(flowInstanceEntity.getInitiatorId(), flowTypeEnum);
    }
}
