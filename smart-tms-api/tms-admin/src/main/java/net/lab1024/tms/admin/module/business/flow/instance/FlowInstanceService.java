package net.lab1024.tms.admin.module.business.flow.instance;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.lab1024.tms.admin.module.business.flow.bussiness.FlowBusinessService;
import net.lab1024.tms.admin.module.business.flow.bussiness.domain.FlowBusinessEndBO;
import net.lab1024.tms.admin.module.business.flow.bussiness.domain.FlowBusinessExtendBO;
import net.lab1024.tms.admin.module.business.flow.config.FlowGatewayConfigService;
import net.lab1024.tms.admin.module.business.flow.config.FlowHandlerConfigService;
import net.lab1024.tms.admin.module.business.flow.constant.FlowInstanceQueryTypeEnum;
import net.lab1024.tms.admin.module.business.flow.constant.FlowTaskTypeEnum;
import net.lab1024.tms.admin.module.business.flow.define.FlowDao;
import net.lab1024.tms.admin.module.business.flow.define.domain.FlowEntity;
import net.lab1024.tms.admin.module.business.flow.instance.dao.FlowInstanceDao;
import net.lab1024.tms.admin.module.business.flow.instance.dao.FlowInstanceTaskDao;
import net.lab1024.tms.admin.module.business.flow.instance.domain.FlowInstanceQueryForm;
import net.lab1024.tms.admin.module.business.flow.instance.domain.FlowInstanceSubmitBO;
import net.lab1024.tms.admin.module.business.flow.instance.domain.FlowInstanceTaskBO;
import net.lab1024.tms.admin.module.business.flow.instance.domain.entity.FlowInstanceEntity;
import net.lab1024.tms.admin.module.business.flow.instance.domain.entity.FlowInstanceTaskEntity;
import net.lab1024.tms.admin.module.business.flow.instance.domain.entity.FlowInstanceTaskRecordEntity;
import net.lab1024.tms.admin.module.business.flow.instance.domain.vo.FlowInstanceTaskVO;
import net.lab1024.tms.admin.module.business.flow.instance.domain.vo.FlowInstanceVO;
import net.lab1024.tms.admin.module.business.flow.instance.manager.FlowInstanceManager;
import net.lab1024.tms.admin.module.business.flow.msg.FlowMsgService;
import net.lab1024.tms.admin.module.business.flow.task.FlowTaskDao;
import net.lab1024.tms.admin.module.business.flow.task.domain.FlowTaskEntity;
import net.lab1024.tms.admin.module.system.department.service.DepartmentService;
import net.lab1024.tms.common.common.code.UserErrorCode;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.exception.BusinessException;
import net.lab1024.tms.common.common.util.SmartBaseEnumUtil;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.common.util.SmartPageUtil;
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
 * @date 2021/8/17 10:28
 */
@Service
public class FlowInstanceService {

    @Autowired
    private FlowDao flowDao;
    @Autowired
    private FlowTaskDao flowTaskDao;
    @Autowired
    private FlowInstanceDao flowInstanceDao;
    @Autowired
    private FlowHandlerConfigService flowHandlerConfigService;
    @Autowired
    private FlowGatewayConfigService flowGatewayConfigService;
    @Autowired
    private FlowInstanceManager flowInstanceManager;
    @Autowired
    private FlowBusinessService flowBusinessService;
    @Autowired
    private FlowMsgService flowMsgService;
    @Autowired
    private FlowInstanceTaskDao flowInstanceTaskDao;
    @Autowired
    private DepartmentService departmentService;

    /**
     * 分页查询流程实例列表
     *
     * @param queryForm
     * @return
     */
    public ResponseDTO<PageResult<FlowInstanceVO>> flowInstanceQuery(FlowInstanceQueryForm queryForm, Long employeeId) {
        Page page = SmartPageUtil.convert2PageQuery(queryForm);
        List<FlowInstanceVO> list = Lists.newArrayList();
        if (FlowInstanceQueryTypeEnum.MY_HANDLE.equalsValue(queryForm.getQueryType())) {
            queryForm.setAuditStatusList(Lists.newArrayList(FlowAuditStatusEnum.PROCESSING.getValue()));
            queryForm.setHandlerId(employeeId);
            list = flowInstanceDao.query(page, queryForm);
        }
        if (FlowInstanceQueryTypeEnum.MY_INITIATE.equalsValue(queryForm.getQueryType())) {
            queryForm.setInitiatorId(employeeId);
            list = flowInstanceDao.query(page, queryForm);
        }
        if (FlowInstanceQueryTypeEnum.MY_RECEIVED.equalsValue(queryForm.getQueryType())) {
            queryForm.setHandlerId(employeeId);
            list = flowInstanceDao.queryReceived(page, queryForm);
        }
        PageResult<FlowInstanceVO> pageResult = SmartPageUtil.convert2PageResult(page, list);
        return ResponseDTO.ok(pageResult);
    }

    /**
     * 撤销一个审批流程
     *
     * @param instanceId
     * @param requestForm 处理人id
     * @return
     */
    public ResponseDTO<String> cancelFlowInstance(Long instanceId, DataTracerRequestForm requestForm) {
        FlowInstanceEntity flowInstanceEntity = flowInstanceDao.selectById(instanceId);
        if (flowInstanceEntity == null) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "对应流程实例信息不存在");
        }
        Long handlerId = requestForm.getOperatorId();
        if (!handlerId.equals(flowInstanceEntity.getInitiatorId())) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "您不是该流程的发起人，无法撤销");
        }
        if (FlowAuditStatusEnum.PASS.equalsValue(flowInstanceEntity.getAuditStatus())) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "此流程已审核通过无法撤销");
        }
        if (FlowAuditStatusEnum.CANCEL.equalsValue(flowInstanceEntity.getAuditStatus())) {
            return ResponseDTO.ok();
        }

        flowInstanceManager.cancelFlowInstance(flowInstanceEntity, requestForm);
        return ResponseDTO.ok();
    }


    /**
     * 获取一批流程实例的当前审批人
     *
     * @param instanceIdList
     * @return
     */
    public Map<Long, String> getInstanceHandlerName(List<Long> instanceIdList) {
        if (CollectionUtils.isEmpty(instanceIdList)) {
            return Maps.newHashMap();
        }
        List<FlowInstanceEntity> instanceEntityList = flowInstanceDao.selectBatchIds(instanceIdList);
        Map<Long, String> handlerNameMap = Maps.newHashMap();
        for (FlowInstanceEntity flowInstanceEntity : instanceEntityList) {
            handlerNameMap.put(flowInstanceEntity.getInstanceId(), flowInstanceEntity.getCurrentHandlerNames());
        }
        return handlerNameMap;
    }

    /**
     * 开启一个审批流
     *
     * @param instanceSubmitBO
     * @return
     */
    public ResponseDTO<Long> startFlowInstance(FlowInstanceSubmitBO instanceSubmitBO, DataTracerRequestForm requestForm) {
        Object businessData = flowBusinessService.getFlowBusinessData(instanceSubmitBO.getFlowTypeEnum(), instanceSubmitBO.getBusinessId(), instanceSubmitBO.getBusinessCode());
        FlowBusinessExtendBO flowBusinessExtendBO = flowBusinessService.getFlowBusinessExtend(instanceSubmitBO.getFlowTypeEnum(), businessData);
        return this.startFlowInstance(instanceSubmitBO, businessData, flowBusinessExtendBO, requestForm);
    }

    /**
     * 开启一个审批流
     *
     * @param instanceSubmitBO
     * @param businessData     非空
     * @return
     */
    public ResponseDTO<Long> startFlowInstance(FlowInstanceSubmitBO instanceSubmitBO, Object businessData, FlowBusinessExtendBO flowBusinessExtendBO, DataTracerRequestForm requestForm) {
        FlowEntity flowEntity = flowDao.selectByFlowType(instanceSubmitBO.getFlowTypeEnum().getValue());
        if (flowEntity == null) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "缺少对应流程定义信息");
        }
        Long initiatorId = instanceSubmitBO.getInitiatorId();
        Long enterpriseId = instanceSubmitBO.getEnterpriseId();
        Long flowId = flowEntity.getFlowId();
        //构建流程实例
        FlowInstanceEntity flowInstanceEntity = this.buildFlowInstance(flowEntity, instanceSubmitBO, businessData, flowBusinessExtendBO);

        List<FlowTaskEntity> taskEntityList = flowTaskDao.selectByFlowId(flowId, enterpriseId);
        //
        if (CollectionUtils.isEmpty(taskEntityList)) {
            flowInstanceEntity.setAuditStatus(FlowAuditStatusEnum.PASS.getValue());
            flowInstanceEntity.setFinishTime(LocalDateTime.now());
            flowInstanceEntity.setCurrentTaskId(0L);
            flowInstanceEntity.setCurrentTaskName("开始");
            flowInstanceEntity.setCurrentHandlers(requestForm.getOperatorId().toString());
            flowInstanceEntity.setCurrentHandlerNames(requestForm.getOperatorName());
            flowInstanceManager.noTaskInstance(instanceSubmitBO.getFlowTypeEnum(), flowInstanceEntity, requestForm);
            return ResponseDTO.ok(flowInstanceEntity.getInstanceId());
        }
        //构建审批任务流
        FlowInstanceTaskBO flowInstanceTask = this.buildFlowInstanceTask(taskEntityList, initiatorId, businessData);
        //审批任务流转流程实例任务
        List<FlowInstanceTaskEntity> instanceTaskEntityList = Lists.newArrayList();
        //待保存的审批记录-自动完成的节点的记录
        List<FlowInstanceTaskRecordEntity> insertTaskRecordList = Lists.newArrayList();
        this.buildFlowInstanceTaskEntity(flowInstanceTask, initiatorId, instanceTaskEntityList, insertTaskRecordList);
        //获取所有未处理的任务节点
        List<FlowInstanceTaskEntity> noHandleTaskList = instanceTaskEntityList.stream().filter(e -> !e.getHandleFlag()).collect(Collectors.toList());
        //是否开始即结束
        Boolean isEnd = false;
        if (CollectionUtils.isEmpty(noHandleTaskList)) {
            FlowInstanceTaskEntity currentTask = instanceTaskEntityList.get(instanceTaskEntityList.size() - 1);
            flowInstanceEntity.setAuditStatus(FlowAuditStatusEnum.PASS.getValue());
            flowInstanceEntity.setFinishTime(LocalDateTime.now());
            flowInstanceEntity.setCurrentTaskId(currentTask.getTaskId());
            flowInstanceEntity.setCurrentTaskName(currentTask.getTaskName());
            flowInstanceEntity.setCurrentHandlers(currentTask.getHandlers());
            flowInstanceEntity.setCurrentHandlerNames(currentTask.getHandlerNames());
            flowInstanceEntity.setCurrentOperateCode(currentTask.getOperateCode());
            isEnd = true;
        } else {
            FlowInstanceTaskEntity currentTask = noHandleTaskList.get(0);
            flowInstanceEntity.setCurrentTaskId(currentTask.getTaskId());
            flowInstanceEntity.setCurrentTaskName(currentTask.getTaskName());
            flowInstanceEntity.setCurrentHandlers(currentTask.getHandlers());
            flowInstanceEntity.setCurrentHandlerNames(currentTask.getHandlerNames());
            flowInstanceEntity.setCurrentOperateCode(currentTask.getOperateCode());
        }
        flowInstanceManager.startInstance(instanceSubmitBO.getFlowTypeEnum(), flowInstanceEntity, instanceTaskEntityList, insertTaskRecordList, requestForm);

        //发送消息
        if (isEnd) {
            FlowAuditStatusEnum auditStatusEnum = SmartBaseEnumUtil.getEnumByValue(flowInstanceEntity.getAuditStatus(), FlowAuditStatusEnum.class);
            flowMsgService.sendAuditResultMsg(flowInstanceEntity.getInstanceId(), flowInstanceEntity.getInitiatorId(), auditStatusEnum);
        } else {
            flowMsgService.sendAuditRemindMsg(flowInstanceEntity.getInstanceId());
        }
        return ResponseDTO.ok(flowInstanceEntity.getInstanceId());
    }


    /**
     * 构建流程实例对象
     *
     * @param instanceSubmitBO
     * @param businessData
     * @return
     */
    private FlowInstanceEntity buildFlowInstance(FlowEntity flowEntity, FlowInstanceSubmitBO instanceSubmitBO, Object businessData, FlowBusinessExtendBO flowBusinessExtendBO) {
        FlowInstanceEntity flowInstanceEntity = new FlowInstanceEntity();
        flowInstanceEntity.setFlowId(flowEntity.getFlowId());
        flowInstanceEntity.setEnterpriseId(instanceSubmitBO.getEnterpriseId());
        flowInstanceEntity.setFlowType(flowEntity.getFlowType());
        flowInstanceEntity.setAuditStatus(FlowAuditStatusEnum.PROCESSING.getValue());
        flowInstanceEntity.setInitiatorId(instanceSubmitBO.getInitiatorId());
        flowInstanceEntity.setBusinessId(instanceSubmitBO.getBusinessId());
        flowInstanceEntity.setBusinessCode(instanceSubmitBO.getBusinessCode());
        if (flowBusinessExtendBO != null) {
            flowInstanceEntity.setExtendField1(flowBusinessExtendBO.getExtendField1());
            flowInstanceEntity.setExtendFieldName1(flowBusinessExtendBO.getExtendFieldName1());
            flowInstanceEntity.setExtendField2(flowBusinessExtendBO.getExtendField2());
            flowInstanceEntity.setExtendFieldName2(flowBusinessExtendBO.getExtendFieldName2());
            flowInstanceEntity.setExtendField3(flowBusinessExtendBO.getExtendField3());
            flowInstanceEntity.setExtendFieldName3(flowBusinessExtendBO.getExtendFieldName3());
            flowInstanceEntity.setExtendField4(flowBusinessExtendBO.getExtendField4());
            flowInstanceEntity.setExtendFieldName4(flowBusinessExtendBO.getExtendFieldName4());
            flowInstanceEntity.setExtendField5(flowBusinessExtendBO.getExtendField5());
            flowInstanceEntity.setExtendFieldName5(flowBusinessExtendBO.getExtendFieldName5());
            flowInstanceEntity.setExtendField6(flowBusinessExtendBO.getExtendField6());
            flowInstanceEntity.setExtendFieldName6(flowBusinessExtendBO.getExtendFieldName6());
            flowInstanceEntity.setExtendField7(flowBusinessExtendBO.getExtendField7());
            flowInstanceEntity.setExtendFieldName7(flowBusinessExtendBO.getExtendFieldName7());
            flowInstanceEntity.setExtendField8(flowBusinessExtendBO.getExtendField8());
            flowInstanceEntity.setExtendFieldName8(flowBusinessExtendBO.getExtendFieldName8());
            flowInstanceEntity.setExtendField9(flowBusinessExtendBO.getExtendField9());
            flowInstanceEntity.setExtendFieldName9(flowBusinessExtendBO.getExtendFieldName9());
            flowInstanceEntity.setExtendField10(flowBusinessExtendBO.getExtendField10());
            flowInstanceEntity.setExtendFieldName10(flowBusinessExtendBO.getExtendFieldName10());
        }
        if (businessData != null) {
            flowInstanceEntity.setBusinessData(JSON.toJSONString(businessData));
        }
        flowInstanceEntity.setRemark(instanceSubmitBO.getRemark());
        return flowInstanceEntity;
    }


    /**
     * 流程实例审批节点
     *
     * @param flowInstanceTask
     * @param instanceTaskEntityList
     */
    private void buildFlowInstanceTaskEntity(FlowInstanceTaskBO flowInstanceTask, Long initiatorId, List<FlowInstanceTaskEntity> instanceTaskEntityList, List<FlowInstanceTaskRecordEntity> insertTaskRecordList) {
        FlowInstanceTaskEntity instanceTaskEntity = flowInstanceTask.getInstanceTask();
        if (!FlowTaskTypeEnum.GATEWAY.equalsValue(instanceTaskEntity.getTaskType())) {
            this.buildInstanceTaskInitValue(flowInstanceTask, initiatorId, insertTaskRecordList);
            instanceTaskEntityList.add(instanceTaskEntity);
        }
        FlowInstanceTaskBO nextTask = flowInstanceTask.getNextTask();
        if (nextTask == null) {
            return;
        }
        this.buildFlowInstanceTaskEntity(nextTask, initiatorId, instanceTaskEntityList, insertTaskRecordList);
    }

    /**
     * 构建流程任务阶段初始值
     *
     * @param flowInstanceTask
     */
    private void buildInstanceTaskInitValue(FlowInstanceTaskBO flowInstanceTask, Long initiatorId, List<FlowInstanceTaskRecordEntity> insertTaskRecordList) {
        FlowInstanceTaskBO preTask = flowInstanceTask.getPreTask();
        if (preTask == null) {
            return;
        }
        //判断本级任务节点是否可以自动完成
        FlowInstanceTaskEntity instanceTask = flowInstanceTask.getInstanceTask();
        if (FlowTaskTypeEnum.APPROVE.equalsValue(instanceTask.getTaskType())) {
            Boolean autoCompleteFlag = this.getApproveTaskAutoCompleteFlag(flowInstanceTask, initiatorId);
            if (autoCompleteFlag) {
                this.completeTask(flowInstanceTask.getInstanceTask(), insertTaskRecordList,FlowAuditStatusEnum.PASS, Lists.newArrayList(initiatorId));
            }
        }
        if (FlowTaskTypeEnum.CC.equalsValue(instanceTask.getTaskType())) {
            Boolean autoCompleteFlag = this.getCcTaskAutoCompleteFlag(flowInstanceTask);
            if (autoCompleteFlag) {
                List<Long> handlerIds = SmartStringUtil.splitConverToLongList(instanceTask.getHandlers(), ",");
                this.completeTask(flowInstanceTask.getInstanceTask(), insertTaskRecordList, FlowAuditStatusEnum.ALREADY_CC, handlerIds);
            }
        }
    }

    private Boolean getApproveTaskAutoCompleteFlag(FlowInstanceTaskBO flowInstanceTask, Long initiatorId) {
        FlowInstanceTaskEntity instanceTask = flowInstanceTask.getInstanceTask();

        List<Long> handlerIds = SmartStringUtil.splitConverToLongList(instanceTask.getHandlers(), ",");
        if(!handlerIds.contains(initiatorId)){
            return false;
        }
        if(StringUtils.isNotBlank(instanceTask.getTaskListener())){
            return false;
        }
        FlowInstanceTaskBO preTask = flowInstanceTask.getPreTask();
        if (preTask == null) {
            return true;
        }
        return getPreCanCompleteFlag(preTask);
    }

    /**
     * 判断节点是否可以自动完成,目前只有抄送节点可以自动完成
     *
     * @param flowInstanceTask
     * @return
     */
    private Boolean getCcTaskAutoCompleteFlag(FlowInstanceTaskBO flowInstanceTask) {
        FlowInstanceTaskBO preTask = flowInstanceTask.getPreTask();
        if (preTask == null) {
            return true;
        }
        return getPreCanCompleteFlag(preTask);
    }

    private Boolean getPreCanCompleteFlag(FlowInstanceTaskBO preTask) {
        FlowInstanceTaskEntity preInstanceTask = preTask.getInstanceTask();
        if (FlowTaskTypeEnum.START.equalsValue(preInstanceTask.getTaskType())) {
            return true;
        }
        if (FlowTaskTypeEnum.APPROVE.equalsValue(preInstanceTask.getTaskType()) && !FlowAuditStatusEnum.PASS.equalsValue(preInstanceTask.getAuditStatus())) {
            return false;
        }
        if (FlowTaskTypeEnum.CC.equalsValue(preInstanceTask.getTaskType()) && !preInstanceTask.getHandleFlag()) {
            return false;
        }
        return getPreCanCompleteFlag(preTask.getPreTask());
    }

    /**
     * 自动完成抄送节点
     *
     * @param instanceTask
     * @param insertTaskRecordList
     */
    private void completeTask(FlowInstanceTaskEntity instanceTask, List<FlowInstanceTaskRecordEntity> insertTaskRecordList, FlowAuditStatusEnum statusEnum, List<Long> handlerIds) {
        instanceTask.setHandleFlag(true);
        instanceTask.setAuditStatus(statusEnum.getValue());
        instanceTask.setFinishTime(LocalDateTime.now());
        for (Long handlerId : handlerIds) {
            FlowInstanceTaskRecordEntity instanceTaskRecordEntity = new FlowInstanceTaskRecordEntity();
            instanceTaskRecordEntity.setEnterpriseId(instanceTask.getEnterpriseId());
            instanceTaskRecordEntity.setInstanceId(instanceTask.getInstanceId());
            instanceTaskRecordEntity.setFlowId(instanceTask.getFlowId());
            instanceTaskRecordEntity.setTaskId(instanceTask.getTaskId());
            instanceTaskRecordEntity.setHandler(handlerId);
            instanceTaskRecordEntity.setAuditStatus(statusEnum.getValue());
            insertTaskRecordList.add(instanceTaskRecordEntity);
        }
    }

    /**
     * 构建审批流实例的任务流
     *
     * @param taskEntityList
     * @param businessData
     * @return
     */
    private FlowInstanceTaskBO buildFlowInstanceTask(List<FlowTaskEntity> taskEntityList, Long initiatorId, Object businessData) {
        Optional<FlowTaskEntity> startTaskOptional = taskEntityList.stream().filter(e -> FlowTaskTypeEnum.START.equalsValue(e.getTaskType())).findFirst();
        if (!startTaskOptional.isPresent()) {
            throw new BusinessException("缺少开始节点");
        }
        FlowInstanceTaskBO flowInstanceTaskBO = new FlowInstanceTaskBO();
        FlowTaskEntity taskEntity = startTaskOptional.get();
        FlowInstanceTaskEntity instanceTaskEntity = this.buildInstanceTask(initiatorId, taskEntity, businessData);
        flowInstanceTaskBO.setInstanceTask(instanceTaskEntity);
        this.buildFlowInstanceTask(flowInstanceTaskBO, taskEntityList, initiatorId, businessData);
        return flowInstanceTaskBO;
    }

    private void buildFlowInstanceTask(FlowInstanceTaskBO preInstanceTaskBO, List<FlowTaskEntity> taskEntityList, Long initiatorId, Object businessData) {
        FlowInstanceTaskEntity preInstanceTaskEntity = preInstanceTaskBO.getInstanceTask();
        List<FlowTaskEntity> taskList = taskEntityList.stream().filter(e -> {
            String parentId = e.getParentIds();
            List<Long> parentIdList = SmartStringUtil.splitConverToLongList(parentId, ",");
            return parentIdList.contains(preInstanceTaskEntity.getTaskId());
        }).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(taskList)) {
            return;
        }
        FlowTaskEntity taskEntity = null;
        //路由节点
        if (taskList.size() > 1) {
            taskEntity = flowGatewayConfigService.getGatewayTask(taskList, businessData);
        } else {
            taskEntity = taskList.get(0);
        }

        FlowInstanceTaskBO nextFlowInstanceTaskBO = new FlowInstanceTaskBO();
        nextFlowInstanceTaskBO.setPreTask(preInstanceTaskBO);
        FlowInstanceTaskEntity instanceTaskEntity = this.buildInstanceTask(initiatorId, taskEntity, businessData);
        nextFlowInstanceTaskBO.setInstanceTask(instanceTaskEntity);

        preInstanceTaskBO.setNextTask(nextFlowInstanceTaskBO);

        this.buildFlowInstanceTask(nextFlowInstanceTaskBO, taskEntityList, initiatorId, businessData);
    }


    private FlowInstanceTaskEntity buildInstanceTask(Long initiatorId, FlowTaskEntity taskEntity, Object businessData) {
        List<Long> handlerIdList = Lists.newArrayList();
        Integer auditStatus = FlowAuditStatusEnum.WAIT.getValue();
        Boolean handleFlag = false;
        LocalDateTime finishTime = null;
        if (FlowTaskTypeEnum.START.equalsValue(taskEntity.getTaskType())) {
            handlerIdList = Lists.newArrayList(initiatorId);
            auditStatus = FlowAuditStatusEnum.PASS.getValue();
            handleFlag = true;
            finishTime = LocalDateTime.now();
        } else {
            handlerIdList = flowHandlerConfigService.getFlowHandler(initiatorId, taskEntity.getEnterpriseId(), taskEntity.getTaskConfig(), businessData);
        }
        if (!FlowTaskTypeEnum.GATEWAY.equalsValue(taskEntity.getTaskType()) && CollectionUtils.isEmpty(handlerIdList)) {
            throw new BusinessException(taskEntity.getTaskName() + "缺少处理人");
        }
        List<String> handlerNameList = flowHandlerConfigService.getHandlerNameList(handlerIdList);
        FlowInstanceTaskEntity flowInstanceTaskEntity = new FlowInstanceTaskEntity();
        flowInstanceTaskEntity.setFlowId(taskEntity.getFlowId());
        flowInstanceTaskEntity.setEnterpriseId(taskEntity.getEnterpriseId());
        flowInstanceTaskEntity.setTaskId(taskEntity.getTaskId());
        flowInstanceTaskEntity.setTaskType(taskEntity.getTaskType());
        flowInstanceTaskEntity.setTaskName(taskEntity.getTaskName());
        flowInstanceTaskEntity.setTaskListener(taskEntity.getTaskListener());
        flowInstanceTaskEntity.setHandlers(StringUtils.join(handlerIdList, ","));
        flowInstanceTaskEntity.setHandlerNames(StringUtils.join(handlerNameList, ","));
        flowInstanceTaskEntity.setHandleFlag(handleFlag);
        flowInstanceTaskEntity.setOperateCode(StringUtils.isEmpty(taskEntity.getOperateCode()) ? "NONE" : taskEntity.getOperateCode());
        flowInstanceTaskEntity.setAuditStatus(auditStatus);
        flowInstanceTaskEntity.setFinishTime(finishTime);
        return flowInstanceTaskEntity;
    }

    /**
     * 查询带我审核的总数量
     *
     * @param employeeId
     * @return
     */
    public ResponseDTO<Integer> waitAuditNum(Long employeeId) {
        Integer waitAudit = flowInstanceDao.countByHandlerAndStatus(employeeId, FlowAuditStatusEnum.PROCESSING.getValue());
        return ResponseDTO.ok(waitAudit);
    }

    /**
     * 获取流程当前所在的审批节点
     *
     * @param instanceId
     * @return
     */
    public ResponseDTO<FlowInstanceTaskVO> flowCurrentTask(Long instanceId) {
        FlowInstanceEntity flowInstanceEntity = flowInstanceDao.selectById(instanceId);
        if (flowInstanceEntity == null) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "流程实例不存在");
        }
        //流程已结束
        if (flowInstanceEntity.getFinishTime() != null) {
            return ResponseDTO.ok(null);
        }
        //当前所有的任务处理节点
        FlowInstanceTaskEntity flowInstanceTaskEntity = flowInstanceTaskDao.selectByInstanceAndTaskId(instanceId, flowInstanceEntity.getCurrentTaskId());
        if (flowInstanceTaskEntity == null) {
            return ResponseDTO.ok(null);
        }
        FlowInstanceTaskVO instanceTaskVO = SmartBeanUtil.copy(flowInstanceTaskEntity, FlowInstanceTaskVO.class);
        return ResponseDTO.ok(instanceTaskVO);
    }
}
