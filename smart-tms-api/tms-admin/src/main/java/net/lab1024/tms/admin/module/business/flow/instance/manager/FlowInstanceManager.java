package net.lab1024.tms.admin.module.business.flow.instance.manager;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.lab1024.tms.admin.module.business.flow.bussiness.FlowBusinessService;
import net.lab1024.tms.admin.module.business.flow.bussiness.domain.FlowBusinessCancelBO;
import net.lab1024.tms.admin.module.business.flow.bussiness.domain.FlowBusinessEndBO;
import net.lab1024.tms.admin.module.business.flow.bussiness.domain.FlowBusinessStartBO;
import net.lab1024.tms.admin.module.business.flow.constant.FlowTypeEnum;
import net.lab1024.tms.admin.module.business.flow.instance.dao.FlowInstanceDao;
import net.lab1024.tms.admin.module.business.flow.instance.domain.entity.FlowInstanceEntity;
import net.lab1024.tms.admin.module.business.flow.instance.domain.entity.FlowInstanceTaskEntity;
import net.lab1024.tms.admin.module.business.flow.instance.domain.entity.FlowInstanceTaskRecordEntity;
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
@Service
public class FlowInstanceManager extends ServiceImpl<FlowInstanceDao, FlowInstanceEntity> {

    @Autowired
    private FlowInstanceTaskManager flowInstanceTaskManager;
    @Autowired
    private FlowInstanceTaskRecordManager flowInstanceTaskRecordManager;
    @Autowired
    private FlowBusinessService flowBusinessService;

    /**
     * 开启一个审批流
     *
     * @param flowTypeEnum
     * @param flowInstanceEntity
     * @param flowInstanceTaskEntityList
     */
    @Transactional(rollbackFor = Throwable.class)
    public void startInstance(FlowTypeEnum flowTypeEnum,
                              FlowInstanceEntity flowInstanceEntity,
                              List<FlowInstanceTaskEntity> flowInstanceTaskEntityList,
                              List<FlowInstanceTaskRecordEntity> insertTaskRecordList,
                              DataTracerRequestForm requestForm) {
        this.getBaseMapper().insert(flowInstanceEntity);
        Long instanceId = flowInstanceEntity.getInstanceId();
        //触发流程开始时业务逻辑处理
        FlowBusinessStartBO startBO = new FlowBusinessStartBO();
        startBO.setFlowInstanceId(instanceId);
        startBO.setBusinessId(flowInstanceEntity.getBusinessId());
        startBO.setBusinessCode(flowInstanceEntity.getBusinessCode());
        flowBusinessService.triggerStart(flowTypeEnum, startBO, requestForm);
        flowInstanceTaskEntityList.forEach(e -> {
            e.setInstanceId(instanceId);
        });
        //保存流程实例的任务信息
        flowInstanceTaskManager.saveBatch(flowInstanceTaskEntityList);
        //保存流程开始的处理记录
        if (CollectionUtils.isNotEmpty(insertTaskRecordList)) {
            insertTaskRecordList.forEach(e -> {
                e.setInstanceId(instanceId);
            });
            flowInstanceTaskRecordManager.saveBatch(insertTaskRecordList);
        }
        //判断流程是否结束
        if (flowInstanceEntity.getFinishTime() != null) {
            //触发流程结束时业务逻辑处理
            FlowAuditStatusEnum auditStatusEnum = SmartBaseEnumUtil.getEnumByValue(flowInstanceEntity.getAuditStatus(), FlowAuditStatusEnum.class);

            FlowBusinessEndBO endBO = new FlowBusinessEndBO();
            endBO.setBusinessId(flowInstanceEntity.getBusinessId());
            endBO.setBusinessCode(flowInstanceEntity.getBusinessCode());
            endBO.setAuditStatusEnum(auditStatusEnum);
            flowBusinessService.triggerEnd(flowTypeEnum, endBO, requestForm);
        }
    }

    @Transactional(rollbackFor = Throwable.class)
    public void noTaskInstance(FlowTypeEnum flowTypeEnum,
                              FlowInstanceEntity flowInstanceEntity,
                              DataTracerRequestForm requestForm) {
        this.getBaseMapper().insert(flowInstanceEntity);
        Long instanceId = flowInstanceEntity.getInstanceId();
        //触发流程开始时业务逻辑处理
        FlowBusinessStartBO startBO = new FlowBusinessStartBO();
        startBO.setFlowInstanceId(instanceId);
        startBO.setBusinessId(flowInstanceEntity.getBusinessId());
        startBO.setBusinessCode(flowInstanceEntity.getBusinessCode());
        flowBusinessService.triggerStart(flowTypeEnum, startBO, requestForm);
        //判断流程是否结束
        if (flowInstanceEntity.getFinishTime() != null) {
            //触发流程结束时业务逻辑处理
            FlowAuditStatusEnum auditStatusEnum = SmartBaseEnumUtil.getEnumByValue(flowInstanceEntity.getAuditStatus(), FlowAuditStatusEnum.class);

            FlowBusinessEndBO endBO = new FlowBusinessEndBO();
            endBO.setBusinessId(flowInstanceEntity.getBusinessId());
            endBO.setBusinessCode(flowInstanceEntity.getBusinessCode());
            endBO.setAuditStatusEnum(auditStatusEnum);
            flowBusinessService.triggerEnd(flowTypeEnum, endBO, requestForm);
        }
    }


    /**
     * 流程撤销
     *
     * @param flowInstanceEntity
     * @param requestForm
     */
    @Transactional(rollbackFor = Throwable.class)
    public void cancelFlowInstance(FlowInstanceEntity flowInstanceEntity, DataTracerRequestForm requestForm) {
        FlowInstanceEntity updateInstanceEntity = new FlowInstanceEntity();
        updateInstanceEntity.setInstanceId(flowInstanceEntity.getInstanceId());
        updateInstanceEntity.setAuditStatus(FlowAuditStatusEnum.CANCEL.getValue());
        updateInstanceEntity.setFinishTime(LocalDateTime.now());
        this.getBaseMapper().updateById(updateInstanceEntity);

        FlowTypeEnum flowTypeEnum = SmartBaseEnumUtil.getEnumByValue(flowInstanceEntity.getFlowType(), FlowTypeEnum.class);
        //触发流程撤销时业务逻辑处理
        FlowBusinessCancelBO cancelBO = new FlowBusinessCancelBO();
        cancelBO.setBusinessId(flowInstanceEntity.getBusinessId());
        cancelBO.setBusinessCode(flowInstanceEntity.getBusinessCode());
        flowBusinessService.triggerCancel(flowTypeEnum, cancelBO, requestForm);
    }

}
