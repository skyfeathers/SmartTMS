package net.lab1024.tms.admin.module.business.flow.comment;

import net.lab1024.tms.admin.module.business.flow.instance.dao.FlowInstanceDao;
import net.lab1024.tms.admin.module.business.flow.instance.dao.FlowInstanceTaskDao;
import net.lab1024.tms.admin.module.business.flow.instance.domain.entity.FlowInstanceEntity;
import net.lab1024.tms.admin.module.business.flow.instance.domain.entity.FlowInstanceTaskEntity;
import net.lab1024.tms.admin.module.business.flow.msg.FlowMsgService;
import net.lab1024.tms.common.common.code.UserErrorCode;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.common.util.SmartStringUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 审批流评论
 *
 * @author lihaifan
 * @date 2022/2/10 16:20
 */
@Service
public class FlowCommentService {

    @Autowired
    private FlowInstanceDao flowInstanceDao;
    @Autowired
    private FlowInstanceTaskDao flowInstanceTaskDao;
    @Autowired
    private FlowCommentDao flowCommentDao;
    @Autowired
    private FlowMsgService flowMsgService;

    /**
     * 新建评论
     *
     * @param createForm
     * @return
     */
    public ResponseDTO<String> addFlowComment(FlowCommentCreateForm createForm) {
        FlowInstanceEntity flowInstanceEntity = flowInstanceDao.selectById(createForm.getInstanceId());
        if (flowInstanceEntity == null) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "审批不存在");
        }
        FlowInstanceTaskEntity flowInstanceTaskEntity = flowInstanceTaskDao.selectByInstanceAndTaskId(createForm.getInstanceId(), createForm.getCurrentTaskId());
        if (flowInstanceTaskEntity == null) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "审批节点不存在");
        }
        if (!flowInstanceEntity.getEnterpriseId().equals(createForm.getEnterpriseId())) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR, "当前登录企业和当前流程所属企业不一致");
        }
        // 查询审批评论接收人
        List<FlowInstanceTaskEntity> flowInstanceTaskEntities = flowInstanceTaskDao.selectByInstanceId(createForm.getInstanceId());
        String receiver = "";
        if (CollectionUtils.isNotEmpty(flowInstanceTaskEntities)) {
            Set<Long> receiverSet = new HashSet<>();
            for (FlowInstanceTaskEntity instanceTaskEntity : flowInstanceTaskEntities) {
                // 只给已处理的 或 当前任务节点 人发送推送
                if (!instanceTaskEntity.getHandleFlag()
                        && !instanceTaskEntity.getInstanceTaskId().equals(flowInstanceTaskEntity.getInstanceTaskId())) {
                    continue;
                }
                // 获取处理人
                String handlers = instanceTaskEntity.getHandlers();
                if (StringUtils.isBlank(handlers)) {
                    continue;
                }
                List<Long> handlerIds = SmartStringUtil.splitConverToLongList(handlers, ",");
                receiverSet.addAll(handlerIds);
            }
            // 从接收人中移除当前登录人
            receiverSet.remove(createForm.getOperatorId());
            // 逗号隔开
            receiver = StringUtils.join(receiverSet, ",");
        }
        // 构建评论参数
        FlowCommentEntity flowCommentEntity = SmartBeanUtil.copy(createForm, FlowCommentEntity.class);
        flowCommentEntity.setCreator(createForm.getOperatorId());
        flowCommentEntity.setInstanceTaskId(flowInstanceTaskEntity.getInstanceTaskId());
        flowCommentEntity.setReceiver(receiver);
        flowCommentEntity.setDeletedFlag(Boolean.FALSE);
        flowCommentDao.insert(flowCommentEntity);
        // 发送微信通知
        flowMsgService.sendAuditCommentMsg(createForm.getInstanceId(), flowCommentEntity.getCommentId(), createForm.getOperatorName());
        return ResponseDTO.ok();
    }
}
