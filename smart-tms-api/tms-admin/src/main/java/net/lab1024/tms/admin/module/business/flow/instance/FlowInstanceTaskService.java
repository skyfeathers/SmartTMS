package net.lab1024.tms.admin.module.business.flow.instance;

import com.google.common.collect.Lists;
import net.lab1024.tms.admin.common.constants.CommonConst;
import net.lab1024.tms.admin.module.business.flow.comment.FlowCommentDao;
import net.lab1024.tms.admin.module.business.flow.comment.FlowCommentVO;
import net.lab1024.tms.admin.module.business.flow.constant.FlowTaskTypeEnum;
import net.lab1024.tms.admin.module.business.flow.instance.dao.FlowInstanceDao;
import net.lab1024.tms.admin.module.business.flow.instance.dao.FlowInstanceTaskDao;
import net.lab1024.tms.admin.module.business.flow.instance.dao.FlowInstanceTaskRecordDao;
import net.lab1024.tms.admin.module.business.flow.instance.domain.FlowInstanceAuditForm;
import net.lab1024.tms.admin.module.business.flow.instance.domain.FlowInstanceBatchAuditForm;
import net.lab1024.tms.admin.module.business.flow.instance.domain.FlowInstanceTaskBO;
import net.lab1024.tms.admin.module.business.flow.instance.domain.entity.FlowInstanceEntity;
import net.lab1024.tms.admin.module.business.flow.instance.domain.entity.FlowInstanceTaskEntity;
import net.lab1024.tms.admin.module.business.flow.instance.domain.entity.FlowInstanceTaskRecordEntity;
import net.lab1024.tms.admin.module.business.flow.instance.domain.vo.FlowInstanceTaskRecordVO;
import net.lab1024.tms.admin.module.business.flow.instance.domain.vo.FlowInstanceTaskVO;
import net.lab1024.tms.admin.module.business.flow.instance.manager.FlowInstanceTaskManager;
import net.lab1024.tms.admin.module.business.flow.listener.TaskAuditListenerBO;
import net.lab1024.tms.admin.module.business.flow.msg.FlowMsgService;
import net.lab1024.tms.admin.module.business.flow.waithandle.FlowWaitHandleDao;
import net.lab1024.tms.admin.module.business.flow.waithandle.FlowWaitHandleEntity;
import net.lab1024.tms.admin.module.system.employee.dao.EmployeeDao;
import net.lab1024.tms.common.common.code.UserErrorCode;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.common.util.SmartStringUtil;
import net.lab1024.tms.common.module.business.flow.FlowAuditStatusEnum;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * [  ]
 *
 * @author yandanyang
 * @date 2021/8/18 10:12
 */
@Service
public class FlowInstanceTaskService {
    @Autowired
    private FlowInstanceDao flowInstanceDao;
    @Autowired
    private FlowInstanceTaskDao flowInstanceTaskDao;
    @Autowired
    private EmployeeDao employeeDao;
    @Autowired
    private FlowInstanceTaskRecordDao flowInstanceTaskRecordDao;
    @Autowired
    private FlowWaitHandleDao flowWaitHandleDao;
    @Autowired
    private FlowInstanceTaskCommonService flowInstanceTaskCommonService;
    @Autowired
    private FlowInstanceTaskManager flowInstanceTaskManager;
    @Autowired
    private FlowMsgService flowMsgService;
    @Autowired
    private FlowCommentDao flowCommentDao;

    /**
     * 查询流程实例的各个任务处理节点信息
     *
     * @param instanceId
     * @return
     */
    public ResponseDTO<List<FlowInstanceTaskVO>> instanceTaskList(Long instanceId) {
        FlowInstanceEntity flowInstanceEntity = flowInstanceDao.selectById(instanceId);
        if (flowInstanceEntity == null) {
            return ResponseDTO.ok(Lists.newArrayList());
        }
        List<FlowInstanceTaskEntity> instanceTaskEntityList = flowInstanceTaskDao.selectByInstanceId(instanceId);
        if (CollectionUtils.isEmpty(instanceTaskEntityList)) {
            return ResponseDTO.ok(Lists.newArrayList());
        }
        List<FlowInstanceTaskVO> instanceTaskVOList = SmartBeanUtil.copyList(instanceTaskEntityList, FlowInstanceTaskVO.class);
        // 如果审批流已撤销给最后一步增加一个撤销节点
        if (FlowAuditStatusEnum.CANCEL.equalsValue(flowInstanceEntity.getAuditStatus())) {
            // 获取流程发起人id与名称
            Optional<FlowInstanceTaskVO> startTask = instanceTaskVOList.stream().filter(e -> FlowTaskTypeEnum.START.equalsValue(e.getTaskType())).findFirst();
            if (startTask.isPresent()) {
                FlowInstanceTaskVO startTaskVo = startTask.get();
                // 创建撤销节点
                FlowInstanceTaskVO cancelTask = new FlowInstanceTaskVO();
                cancelTask.setInstanceId(flowInstanceEntity.getInstanceId());
                cancelTask.setFlowId(flowInstanceEntity.getFlowId());
                cancelTask.setTaskType(FlowTaskTypeEnum.APPROVE.getValue());
                cancelTask.setHandlerNames(startTaskVo.getHandlerNames());
                cancelTask.setHandlers(startTaskVo.getHandlers());
                cancelTask.setHandleFlag(Boolean.TRUE);
                cancelTask.setAuditStatus(FlowAuditStatusEnum.CANCEL.getValue());
                cancelTask.setFinishTime(flowInstanceEntity.getFinishTime());
                instanceTaskVOList.add(cancelTask);
            }
        }
        this.buildAuditRecordHandler(instanceId, instanceTaskVOList);
        // 查询该审批评论列表
        List<FlowCommentVO> flowCommentVOS = flowCommentDao.selectByInstanceId(instanceId, Boolean.FALSE);
        if (CollectionUtils.isEmpty(flowCommentVOS)) {
            return ResponseDTO.ok(instanceTaskVOList);
        }
        // 给最后一个任务节点塞一个评论列表
        instanceTaskVOList.get(instanceTaskVOList.size() - 1).setCommentList(flowCommentVOS);
        return ResponseDTO.ok(instanceTaskVOList);
    }

    private void buildAuditRecordHandler(Long instanceId, List<FlowInstanceTaskVO> instanceTaskVOList){
        List<Long> alreadyHandleTaskId = instanceTaskVOList.stream().filter(e -> e.getHandleFlag()).map(FlowInstanceTaskVO::getTaskId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(alreadyHandleTaskId)) {
            return;
        }
        List<FlowInstanceTaskRecordVO> taskRecordList = flowInstanceTaskRecordDao.selectByTaskId(instanceId, alreadyHandleTaskId);
        if (CollectionUtils.isEmpty(taskRecordList)) {
           return;
        }
        Map<Long, List<String>> taskRecordEntityListMap = taskRecordList.stream().collect(Collectors.groupingBy(FlowInstanceTaskRecordVO::getTaskId,
                Collectors.mapping(FlowInstanceTaskRecordVO::getHandlerName, Collectors.toList())));
        instanceTaskVOList.forEach(e -> {
            List<String> handlerNameList = taskRecordEntityListMap.get(e.getTaskId());
            if (CollectionUtils.isNotEmpty(handlerNameList)) {
                e.setAuditRecordHandlerNames(StringUtils.join(handlerNameList, ","));
            }
        });
    }

    /**
     * 获取流程实例的处理人id
     *
     * @param instanceId
     * @return
     */
    public ResponseDTO<List<Long>> instanceTaskCurrentHandler(Long instanceId) {
        FlowInstanceEntity flowInstanceEntity = flowInstanceDao.selectById(instanceId);
        if (flowInstanceEntity == null) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "对应流程实例信息不存在");
        }
        List<Long> handlerIds = Lists.newArrayList();
        if (FlowAuditStatusEnum.PROCESSING.equalsValue(flowInstanceEntity.getAuditStatus())) {
            handlerIds = SmartStringUtil.splitConverToLongList(flowInstanceEntity.getCurrentHandlers(), ",");
        }
        return ResponseDTO.ok(handlerIds);
    }

    /**
     * 获取流程实例的处理人id
     *
     * @param flowInstanceEntity
     * @param instanceTaskEntityList
     * @return
     */
    private FlowInstanceTaskEntity getCurrentInstanceTask(FlowInstanceEntity flowInstanceEntity, List<FlowInstanceTaskEntity> instanceTaskEntityList) {
        //当前任务节点
        Long currentTaskId = flowInstanceEntity.getCurrentTaskId();
        Optional<FlowInstanceTaskEntity> instanceTaskEntityOptional = instanceTaskEntityList.stream().filter(e -> !e.getHandleFlag() && e.getTaskId().equals(currentTaskId)).findFirst();
        if (!instanceTaskEntityOptional.isPresent()) {
            return null;
        }
        FlowInstanceTaskEntity instanceTaskEntity = instanceTaskEntityOptional.get();
        return instanceTaskEntity;
    }

    /**
     * 审核
     *
     * @param auditForm
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> instanceTaskAudit(FlowInstanceAuditForm auditForm, DataTracerRequestForm dataTracerRequestForm) {
        if (FlowAuditStatusEnum.PASS.equalsValue(auditForm.getAuditStatus())) {
            return this.instanceTaskPass(auditForm, dataTracerRequestForm);
        }
        if (FlowAuditStatusEnum.REJECT.equalsValue(auditForm.getAuditStatus())) {
            return this.instanceTaskReject(auditForm, dataTracerRequestForm);
        }
        return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "只能审核通过或驳回");
    }

    /**
     * 审核
     *
     * @param batchAuditForm
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> instanceTaskBatchAudit(FlowInstanceBatchAuditForm batchAuditForm, DataTracerRequestForm dataTracerRequestForm) {
        // 审核失败的流程实例
        List<Long> auditErrorInstanceIdList = Lists.newArrayList();
        for (Long instanceId : batchAuditForm.getInstanceIdList()) {
            FlowInstanceAuditForm auditForm = new FlowInstanceAuditForm();
            auditForm.setInstanceId(instanceId);
            auditForm.setAuditStatus(batchAuditForm.getAuditStatus());
            auditForm.setAuditRemark(batchAuditForm.getAuditRemark());
            auditForm.setExtData(batchAuditForm.getExtData());
            //循环调用
            ResponseDTO<String> auditResp = ResponseDTO.ok();
            if (FlowAuditStatusEnum.PASS.equalsValue(auditForm.getAuditStatus())) {
                auditResp = this.instanceTaskPass(auditForm, dataTracerRequestForm);
            }
            if (FlowAuditStatusEnum.REJECT.equalsValue(auditForm.getAuditStatus())) {
                auditResp = this.instanceTaskReject(auditForm, dataTracerRequestForm);
            }
            if (!auditResp.getOk()) {
                auditErrorInstanceIdList.add(instanceId);
            }
        }
        if (CollectionUtils.isEmpty(auditErrorInstanceIdList)) {
            return ResponseDTO.ok();
        }
        List<FlowInstanceEntity> instanceEntityList = flowInstanceDao.selectBatchIds(auditErrorInstanceIdList);
        String businessCodes = instanceEntityList.stream().map(FlowInstanceEntity::getBusinessCode).collect(Collectors.joining(","));
        return ResponseDTO.error(UserErrorCode.PARAM_ERROR, businessCodes + "审批失败，请进行单独操作，查看具体原因");
    }

    /**
     * 任务节点审核通过
     * 当前不考虑-》会签
     * 一个人审批通过就全部通过
     *
     * @param auditForm
     * @param dataTracerRequestForm
     */
    public ResponseDTO<String> instanceTaskPass(FlowInstanceAuditForm auditForm, DataTracerRequestForm dataTracerRequestForm) {
        Long instanceId = auditForm.getInstanceId();
        String auditRemark = auditForm.getAuditRemark();
        FlowInstanceEntity flowInstanceEntity = flowInstanceDao.selectById(instanceId);
        if (flowInstanceEntity == null) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "对应流程实例信息不存在");
        }
        if (flowInstanceEntity.getFinishTime() != null) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "对应流程实例已审批结束，请勿重复操作");
        }
        //流程实例的所有任务节点
        List<FlowInstanceTaskEntity> instanceTaskEntityList = flowInstanceTaskDao.selectByInstanceId(instanceId);
        //流程实例的当前任务节点
        FlowInstanceTaskEntity currentInstanceTaskEntity = this.getCurrentInstanceTask(flowInstanceEntity, instanceTaskEntityList);
        if (currentInstanceTaskEntity == null) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "流程实例任务节点信息异常，暂未找到对应的任务处理节点");
        }
        List<Long> handlerIds = SmartStringUtil.splitConverToLongList(flowInstanceEntity.getCurrentHandlers(), ",");
        Long handlerId = dataTracerRequestForm.getOperatorId();
        if (!handlerIds.contains(handlerId)) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "您不是该任务节点的处理人");
        }
        //任务上下任务节点信息
        Map<Long, FlowInstanceTaskBO> flowInstanceTaskStepMap = flowInstanceTaskCommonService.getFlowInstanceTaskStepMap(instanceTaskEntityList);
        FlowInstanceTaskBO flowInstanceTaskBO = flowInstanceTaskStepMap.get(currentInstanceTaskEntity.getTaskId());
        //待更新的实例任务节点
        List<FlowInstanceTaskEntity> updateInstanceTaskList = Lists.newArrayList();
        //待保存的审批记录
        List<FlowInstanceTaskRecordEntity> insertTaskRecordList = Lists.newArrayList();
        //当前任务节点状态更新
        FlowInstanceTaskEntity updateCurrentFlowInstanceTask = new FlowInstanceTaskEntity();
        updateCurrentFlowInstanceTask.setInstanceTaskId(currentInstanceTaskEntity.getInstanceTaskId());
        updateCurrentFlowInstanceTask.setHandleFlag(true);
        updateCurrentFlowInstanceTask.setAuditStatus(FlowAuditStatusEnum.PASS.getValue());
        updateCurrentFlowInstanceTask.setAuditRemark(auditRemark);
        updateCurrentFlowInstanceTask.setFinishTime(LocalDateTime.now());
        updateInstanceTaskList.add(updateCurrentFlowInstanceTask);
        //审批记录
        FlowInstanceTaskRecordEntity instanceTaskRecordEntity = new FlowInstanceTaskRecordEntity();
        instanceTaskRecordEntity.setInstanceId(instanceId);
        instanceTaskRecordEntity.setEnterpriseId(flowInstanceEntity.getEnterpriseId());
        instanceTaskRecordEntity.setFlowId(currentInstanceTaskEntity.getFlowId());
        instanceTaskRecordEntity.setTaskId(currentInstanceTaskEntity.getTaskId());
        instanceTaskRecordEntity.setHandler(handlerId);
        instanceTaskRecordEntity.setAuditStatus(FlowAuditStatusEnum.PASS.getValue());
        instanceTaskRecordEntity.setAuditRemark(auditRemark);
        insertTaskRecordList.add(instanceTaskRecordEntity);
        //任务监听
        List<TaskAuditListenerBO> taskAuditListenerList = Lists.newArrayList();
        TaskAuditListenerBO taskAuditListenerBO = new TaskAuditListenerBO();
        taskAuditListenerBO.setHandlerIdIdList(Lists.newArrayList(handlerId));
        taskAuditListenerBO.setTaskListener(currentInstanceTaskEntity.getTaskListener());
        taskAuditListenerBO.setBusinessId(flowInstanceEntity.getBusinessId());
        taskAuditListenerBO.setBusinessCode(flowInstanceEntity.getBusinessCode());
        taskAuditListenerBO.setExtData(auditForm.getExtData());
        taskAuditListenerList.add(taskAuditListenerBO);

        //构建下一任务自动完成的节点信息
        this.buildWaitUpdateNextFlowInstanceTask(flowInstanceEntity, flowInstanceTaskBO.getNextTask(), updateInstanceTaskList, insertTaskRecordList, taskAuditListenerList, auditForm.getExtData());
        //获取此次处理的所有任务节点id
        List<Long> handleInstanceTaskIdList = updateInstanceTaskList.stream().map(FlowInstanceTaskEntity::getInstanceTaskId).collect(Collectors.toList());
        //获取剩余待处理的任务
        List<FlowInstanceTaskEntity> remainInstanceTaskEntityList = instanceTaskEntityList.stream().filter(e -> !handleInstanceTaskIdList.contains(e.getInstanceTaskId()) && !e.getHandleFlag()).collect(Collectors.toList());
        //流程是否结束
        Boolean flowEndFlag = false;
        if (CollectionUtils.isEmpty(remainInstanceTaskEntityList)) {
            flowEndFlag = true;
        }


        //获取下一 任务处理节点
        FlowInstanceTaskEntity nextTask = this.getNextTask(flowInstanceTaskBO);
        flowInstanceTaskManager.instanceTaskPass(flowInstanceEntity, updateInstanceTaskList, insertTaskRecordList, nextTask, taskAuditListenerList, flowEndFlag, dataTracerRequestForm);
        //发送审核通过消息给发起人
        flowMsgService.sendAuditResultMsg(instanceId, handlerId, FlowAuditStatusEnum.PASS);
        if (nextTask != null) {
            //给下一 任务处理人消息
            flowMsgService.sendAuditRemindMsg(instanceId);
        }
        return ResponseDTO.ok();
    }

    /**
     * 获取下一审批节点
     *
     * @param flowInstanceTaskBO
     * @return
     */
    private FlowInstanceTaskEntity getNextTask(FlowInstanceTaskBO flowInstanceTaskBO) {
        FlowInstanceTaskBO nextInstanceTask = flowInstanceTaskBO.getNextTask();
        if (nextInstanceTask == null) {
            return null;
        }
        FlowInstanceTaskEntity instanceTask = nextInstanceTask.getInstanceTask();
        if (FlowTaskTypeEnum.APPROVE.equalsValue(instanceTask.getTaskType())) {
            return instanceTask;
        }
        return this.getNextTask(nextInstanceTask);
    }

    private void buildWaitUpdateNextFlowInstanceTask(FlowInstanceEntity flowInstanceEntity,
                                                     FlowInstanceTaskBO flowInstanceTaskBO,
                                                     List<FlowInstanceTaskEntity> updateInstanceTaskList,
                                                     List<FlowInstanceTaskRecordEntity> insertTaskRecordList,
                                                     List<TaskAuditListenerBO> taskAuditListenerList,
                                                     String extData) {
        if (flowInstanceTaskBO == null) {
            return;
        }
        FlowInstanceTaskEntity instanceTask = flowInstanceTaskBO.getInstanceTask();
        List<Long> handlerIds = SmartStringUtil.splitConverToLongList(instanceTask.getHandlers(), ",");

        if (FlowTaskTypeEnum.CC.equalsValue(instanceTask.getTaskType())) {
            this.buildWaitUpdateNextFlowInstanceTaskAndRecord(updateInstanceTaskList, insertTaskRecordList, taskAuditListenerList, instanceTask, flowInstanceEntity, FlowAuditStatusEnum.ALREADY_CC, handlerIds, extData);
            this.buildWaitUpdateNextFlowInstanceTask(flowInstanceEntity, flowInstanceTaskBO.getNextTask(), updateInstanceTaskList, insertTaskRecordList, taskAuditListenerList, extData);
        }
        //下一节点是发起人审核 自动通过
        Long initiatorId = flowInstanceEntity.getInitiatorId();
        if (FlowTaskTypeEnum.APPROVE.equalsValue(instanceTask.getTaskType()) && handlerIds.contains(initiatorId) && StringUtils.isBlank(instanceTask.getOperateCode())) {
            this.buildWaitUpdateNextFlowInstanceTaskAndRecord(updateInstanceTaskList, insertTaskRecordList, taskAuditListenerList, instanceTask, flowInstanceEntity, FlowAuditStatusEnum.PASS, Lists.newArrayList(initiatorId), extData);
            this.buildWaitUpdateNextFlowInstanceTask(flowInstanceEntity, flowInstanceTaskBO.getNextTask(), updateInstanceTaskList, insertTaskRecordList, taskAuditListenerList, extData);
        }
    }

    private void buildWaitUpdateNextFlowInstanceTaskAndRecord(List<FlowInstanceTaskEntity> updateInstanceTaskList,
                                                              List<FlowInstanceTaskRecordEntity> insertTaskRecordList,
                                                              List<TaskAuditListenerBO> taskAuditListenerList,
                                                              FlowInstanceTaskEntity instanceTask,
                                                              FlowInstanceEntity flowInstanceEntity,
                                                              FlowAuditStatusEnum auditStatusEnum,
                                                              List<Long> handlerIds,
                                                              String extData) {
        FlowInstanceTaskEntity updateFlowInstanceTask = new FlowInstanceTaskEntity();
        updateFlowInstanceTask.setInstanceTaskId(instanceTask.getInstanceTaskId());
        updateFlowInstanceTask.setHandleFlag(true);
        updateFlowInstanceTask.setAuditStatus(auditStatusEnum.getValue());
        updateFlowInstanceTask.setFinishTime(LocalDateTime.now());
        updateFlowInstanceTask.setTaskListener(instanceTask.getTaskListener());
        updateInstanceTaskList.add(updateFlowInstanceTask);

        TaskAuditListenerBO taskAuditListenerBO = new TaskAuditListenerBO();
        taskAuditListenerBO.setHandlerIdIdList(handlerIds);
        taskAuditListenerBO.setTaskListener(instanceTask.getTaskListener());
        taskAuditListenerBO.setBusinessId(flowInstanceEntity.getBusinessId());
        taskAuditListenerBO.setBusinessCode(flowInstanceEntity.getBusinessCode());
        taskAuditListenerBO.setExtData(extData);
        taskAuditListenerList.add(taskAuditListenerBO);

        for (Long handlerId : handlerIds) {
            FlowInstanceTaskRecordEntity instanceTaskRecordEntity = new FlowInstanceTaskRecordEntity();
            instanceTaskRecordEntity.setInstanceId(instanceTask.getInstanceId());
            instanceTaskRecordEntity.setEnterpriseId(instanceTask.getEnterpriseId());
            instanceTaskRecordEntity.setFlowId(instanceTask.getFlowId());
            instanceTaskRecordEntity.setTaskId(instanceTask.getTaskId());
            instanceTaskRecordEntity.setHandler(handlerId);
            instanceTaskRecordEntity.setAuditStatus(auditStatusEnum.getValue());
            insertTaskRecordList.add(instanceTaskRecordEntity);
        }
    }

    /**
     * 审批驳回
     *
     * @param auditForm
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> instanceTaskReject(FlowInstanceAuditForm auditForm, DataTracerRequestForm dataTracerRequestForm) {
        Long instanceId = auditForm.getInstanceId();
        String auditRemark = auditForm.getAuditRemark();
        FlowInstanceEntity flowInstanceEntity = flowInstanceDao.selectById(instanceId);
        if (flowInstanceEntity == null) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "对应流程实例信息不存在");
        }
        if (flowInstanceEntity.getFinishTime() != null) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "对应流程实例已审批结束，请勿重复操作");
        }
        //流程实例的所有任务节点
        List<FlowInstanceTaskEntity> instanceTaskEntityList = flowInstanceTaskDao.selectByInstanceId(instanceId);
        //流程实例的当前任务节点
        FlowInstanceTaskEntity currentInstanceTaskEntity = this.getCurrentInstanceTask(flowInstanceEntity, instanceTaskEntityList);
        if (currentInstanceTaskEntity == null) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "流程实例任务节点信息异常，暂未找到对应的任务处理节点");
        }
        List<Long> handlerIds = SmartStringUtil.splitConverToLongList(currentInstanceTaskEntity.getHandlers(), ",");
        Long handlerId = dataTracerRequestForm.getOperatorId();
        if (!handlerIds.contains(handlerId)) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "您不是该任务节点的处理人");
        }
        //初始化待办信息
        synchronized (CommonConst.STRING_POOL.intern(flowInstanceEntity.getInitiatorId().toString())) {
            FlowWaitHandleEntity flowWaitHandleEntity = flowWaitHandleDao.selectByEmployeeIdAndFlowType(flowInstanceEntity.getInitiatorId(), flowInstanceEntity.getFlowType());
            if (flowWaitHandleEntity == null) {
                flowWaitHandleEntity = new FlowWaitHandleEntity();
                flowWaitHandleEntity.setEmployeeId(flowInstanceEntity.getInitiatorId());
                flowWaitHandleEntity.setFlowType(flowInstanceEntity.getFlowType());
                flowWaitHandleEntity.setNum(0);
                flowWaitHandleDao.insert(flowWaitHandleEntity);
            }
        }
        //待更新的任务节点
        FlowInstanceTaskEntity updateFlowInstanceTask = new FlowInstanceTaskEntity();
        updateFlowInstanceTask.setInstanceTaskId(currentInstanceTaskEntity.getInstanceTaskId());
        updateFlowInstanceTask.setHandleFlag(true);
        updateFlowInstanceTask.setAuditStatus(FlowAuditStatusEnum.REJECT.getValue());
        updateFlowInstanceTask.setAuditRemark(auditRemark);
        updateFlowInstanceTask.setFinishTime(LocalDateTime.now());
        //待保存的任务审批记录
        FlowInstanceTaskRecordEntity instanceTaskRecordEntity = new FlowInstanceTaskRecordEntity();
        instanceTaskRecordEntity.setInstanceId(instanceId);
        instanceTaskRecordEntity.setEnterpriseId(currentInstanceTaskEntity.getEnterpriseId());
        instanceTaskRecordEntity.setFlowId(currentInstanceTaskEntity.getFlowId());
        instanceTaskRecordEntity.setTaskId(currentInstanceTaskEntity.getTaskId());
        instanceTaskRecordEntity.setHandler(handlerId);
        instanceTaskRecordEntity.setAuditStatus(FlowAuditStatusEnum.REJECT.getValue());
        instanceTaskRecordEntity.setAuditRemark(auditRemark);
        //任务监听
        TaskAuditListenerBO taskAuditListenerBO = new TaskAuditListenerBO();
        taskAuditListenerBO.setHandlerIdIdList(Lists.newArrayList(handlerId));
        taskAuditListenerBO.setTaskListener(currentInstanceTaskEntity.getTaskListener());
        taskAuditListenerBO.setBusinessId(flowInstanceEntity.getBusinessId());
        taskAuditListenerBO.setBusinessCode(flowInstanceEntity.getBusinessCode());
        taskAuditListenerBO.setExtData(auditForm.getExtData());

        flowInstanceTaskManager.instanceTaskReject(flowInstanceEntity, updateFlowInstanceTask, instanceTaskRecordEntity, taskAuditListenerBO, dataTracerRequestForm);

        //发送审核驳回
        flowMsgService.sendAuditResultMsg(instanceId, handlerId, FlowAuditStatusEnum.REJECT);
        return ResponseDTO.ok();
    }


}
