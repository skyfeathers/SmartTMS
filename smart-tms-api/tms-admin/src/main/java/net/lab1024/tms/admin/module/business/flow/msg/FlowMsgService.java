package net.lab1024.tms.admin.module.business.flow.msg;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.tms.admin.common.constants.CommonConst;
import net.lab1024.tms.admin.module.business.flow.comment.FlowCommentDao;
import net.lab1024.tms.admin.module.business.flow.comment.FlowCommentEntity;
import net.lab1024.tms.admin.module.business.flow.instance.dao.FlowInstanceDao;
import net.lab1024.tms.admin.module.business.flow.instance.domain.entity.FlowInstanceEntity;
import net.lab1024.tms.common.common.util.SmartStringUtil;
import net.lab1024.tms.common.module.business.flow.FlowAuditStatusEnum;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * [  ]
 * 发送流程消息
 * 发起审批 -》 发送消息给下一任务处理人
 * 流程审核通过 -》 发送消息给发起人 以及 下一任务处理人
 * 流程审核驳回 -》 发送消息给发起人
 *
 * @author yandanyang
 * @date 2021/10/15 14:14
 */
@Slf4j
@Service
public class FlowMsgService {

    @Autowired
    private FlowInstanceDao flowInstanceDao;

    @Autowired
    private FlowMsgBuildService flowMsgBuildService;

    @Autowired
    private FlowCommentDao flowCommentDao;

    @Autowired(required = false)
    private IFlowMsgSendService flowMsgSendService;
    /**
     * 发送审核提醒消息 给下一任务处理人
     *
     * @param
     */
    public <T> void sendAuditRemindMsg(Long instanceId) {
        if(flowMsgSendService == null){
            log.warn("暂未审批集成消息发送");
            return;
        }
        FlowInstanceEntity flowInstanceEntity = flowInstanceDao.selectById(instanceId);
        // 发送消息给下一任务处理人
        List<Long> toEmployeeIdList = SmartStringUtil.splitConverToLongList(flowInstanceEntity.getCurrentHandlers(), ",");
        // 获取消息体
        IFlowMsgBuildHandleService<T> msgBuildHandleService = flowMsgBuildService.getMsgBuildHandle(flowInstanceEntity.getFlowId());
        if (msgBuildHandleService == null) {
            return;
        }
        T msgNoticeDTO = msgBuildHandleService.auditRemindMsg(flowInstanceEntity);
        // 发送消息
        flowMsgSendService.sendMsg(toEmployeeIdList, flowInstanceEntity, msgNoticeDTO);
    }


    /**
     * 发送审核结果消息 给流程发起人
     *
     * @param instanceId
     * @param auditorId
     */
    public <T> void sendAuditResultMsg(Long instanceId, Long auditorId, FlowAuditStatusEnum auditStatusEnum) {
        if(flowMsgSendService == null){
            log.warn("暂未审批集成消息发送");
            return;
        }
        FlowInstanceEntity flowInstanceEntity = flowInstanceDao.selectById(instanceId);
        //发送消息给流程申请人
        List<Long> toEmployeeIdList = Lists.newArrayList(flowInstanceEntity.getInitiatorId());
        // 获取消息体
        IFlowMsgBuildHandleService<T> msgBuildHandleService = flowMsgBuildService.getMsgBuildHandle(flowInstanceEntity.getFlowId());
        if (msgBuildHandleService == null) {
            return;
        }
        T msgNoticeDTO = msgBuildHandleService.auditResultMsg(flowInstanceEntity, auditorId, auditStatusEnum);
        //发送消息
        flowMsgSendService.sendMsg(toEmployeeIdList, flowInstanceEntity, msgNoticeDTO);
    }

    /**
     * 发送评论给接收人
     * @param instanceId
     * @param commentId
     * @param operatorName
     */
    public <T> void sendAuditCommentMsg(Long instanceId, Long commentId,String operatorName) {
        if(flowMsgSendService == null){
            log.warn("暂未审批集成消息发送");
            return;
        }
        FlowInstanceEntity flowInstanceEntity = flowInstanceDao.selectById(instanceId);
        if(flowInstanceEntity == null){
            return;
        }
        FlowCommentEntity flowCommentEntity = flowCommentDao.selectById(commentId);
        if(flowCommentEntity == null){
            return;
        }
        // 发送消息给接收人
        String receiver = flowCommentEntity.getReceiver();
        List<Long> toEmployeeIdList = SmartStringUtil.splitConverToLongList(receiver, CommonConst.SEPARATOR);
        if(CollectionUtils.isEmpty(toEmployeeIdList)){
            return;
        }
        // 获取消息体
        IFlowMsgBuildHandleService<T> msgBuildHandleService = flowMsgBuildService.getMsgBuildHandle(flowInstanceEntity.getFlowId());
        if (msgBuildHandleService == null) {
            return;
        }
        T msgNoticeDTO = msgBuildHandleService.sendAuditCommentMsg(flowInstanceEntity, flowCommentEntity, operatorName);
        //发送消息
        flowMsgSendService.sendMsg(toEmployeeIdList, flowInstanceEntity, msgNoticeDTO);
    }

}
