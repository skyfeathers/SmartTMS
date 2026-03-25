package net.lab1024.tms.admin.module.business.flow.instance;

import com.google.common.collect.Maps;
import net.lab1024.tms.admin.module.business.flow.instance.dao.FlowInstanceTaskDao;
import net.lab1024.tms.admin.module.business.flow.instance.domain.FlowInstanceTaskBO;
import net.lab1024.tms.admin.module.business.flow.instance.domain.entity.FlowInstanceTaskEntity;
import net.lab1024.tms.common.common.exception.BusinessException;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * [  ]
 *
 * @author yandanyang
 * @date 2021/8/18 11:44
 */
@Service
public class FlowInstanceTaskCommonService {

    @Autowired
    private FlowInstanceTaskDao flowInstanceTaskDao;


    /**
     * 构建审批流任务节点 上一节点，当前节点，下一节点
     * @param instanceId
     * @return
     */
    public Map<Long, FlowInstanceTaskBO> getFlowInstanceTaskStepMap(Long instanceId){
        FlowInstanceTaskBO flowInstanceTaskBO = this.buildFlowInstanceTask(instanceId);
        Map<Long,FlowInstanceTaskBO> flowInstanceTaskStepMap = Maps.newHashMap();

        FlowInstanceTaskEntity instanceTask = flowInstanceTaskBO.getInstanceTask();
        flowInstanceTaskStepMap.put(instanceTask.getTaskId(),flowInstanceTaskBO);
        this.buildFlowInstanceTaskStepMap(flowInstanceTaskBO.getNextTask(),flowInstanceTaskStepMap);
        return flowInstanceTaskStepMap;
    }

    public Map<Long,FlowInstanceTaskBO> getFlowInstanceTaskStepMap(List<FlowInstanceTaskEntity> instanceTaskEntityList){
        FlowInstanceTaskBO flowInstanceTaskBO = this.buildFlowInstanceTask(instanceTaskEntityList);
        Map<Long,FlowInstanceTaskBO> flowInstanceTaskStepMap = Maps.newHashMap();

        FlowInstanceTaskEntity instanceTask = flowInstanceTaskBO.getInstanceTask();
        flowInstanceTaskStepMap.put(instanceTask.getTaskId(),flowInstanceTaskBO);
        this.buildFlowInstanceTaskStepMap(flowInstanceTaskBO.getNextTask(),flowInstanceTaskStepMap);
        return flowInstanceTaskStepMap;
    }

    private void buildFlowInstanceTaskStepMap(FlowInstanceTaskBO flowInstanceTaskBO,Map<Long,FlowInstanceTaskBO> flowInstanceTaskStepMap){
        if(flowInstanceTaskBO == null){
            return;
        }
        FlowInstanceTaskEntity instanceTask = flowInstanceTaskBO.getInstanceTask();
        flowInstanceTaskStepMap.put(instanceTask.getTaskId(),flowInstanceTaskBO);
        this.buildFlowInstanceTaskStepMap(flowInstanceTaskBO.getNextTask(),flowInstanceTaskStepMap);
    }
    /**
     * 构建审批流处理节点 -》 上一节点，当前节点，下一节点
     *
     * @param instanceId
     * @return
     */
    public FlowInstanceTaskBO buildFlowInstanceTask(Long instanceId) {
        List<FlowInstanceTaskEntity> instanceTaskEntityList = flowInstanceTaskDao.selectByInstanceId(instanceId);
        return this.buildFlowInstanceTask(instanceTaskEntityList);
    }

    /**
     * 构建审批流处理节点 -》 上一节点，当前节点，下一节点
     * @param instanceTaskEntityList
     * @return
     */
    public FlowInstanceTaskBO buildFlowInstanceTask(List<FlowInstanceTaskEntity> instanceTaskEntityList) {
        if (CollectionUtils.isEmpty(instanceTaskEntityList)) {
            throw new BusinessException("流程实例任务节点不能为空");
        }
        int step = 0;
        FlowInstanceTaskBO flowInstanceTaskBO = new FlowInstanceTaskBO();
        flowInstanceTaskBO.setInstanceTask(instanceTaskEntityList.get(step));
        this.buildFlowInstanceTask(flowInstanceTaskBO, step, instanceTaskEntityList);
        return flowInstanceTaskBO;
    }

    private void buildFlowInstanceTask(FlowInstanceTaskBO preFlowInstanceTaskBO, int step, List<FlowInstanceTaskEntity> instanceTaskEntityList) {
        int nextStep = step + 1;
        if (nextStep >= instanceTaskEntityList.size()) {
            return;
        }
        FlowInstanceTaskBO nextFlowInstanceTaskBO = new FlowInstanceTaskBO();
        nextFlowInstanceTaskBO.setPreTask(preFlowInstanceTaskBO);
        nextFlowInstanceTaskBO.setInstanceTask(instanceTaskEntityList.get(nextStep));
        preFlowInstanceTaskBO.setNextTask(nextFlowInstanceTaskBO);

        this.buildFlowInstanceTask(nextFlowInstanceTaskBO, nextStep, instanceTaskEntityList);
    }
}
