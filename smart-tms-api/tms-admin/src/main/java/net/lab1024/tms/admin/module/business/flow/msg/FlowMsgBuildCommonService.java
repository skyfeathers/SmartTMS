package net.lab1024.tms.admin.module.business.flow.msg;

import com.google.common.collect.Lists;
import net.lab1024.tms.admin.module.business.flow.comment.FlowCommentEntity;
import net.lab1024.tms.admin.module.business.flow.constant.FlowTypeEnum;
import net.lab1024.tms.admin.module.business.flow.instance.domain.entity.FlowInstanceEntity;
import net.lab1024.tms.admin.module.system.employee.dao.EmployeeDao;
import net.lab1024.tms.common.common.util.SmartBaseEnumUtil;
import net.lab1024.tms.common.module.business.flow.FlowAuditStatusEnum;
import net.lab1024.tms.common.module.system.employee.domain.EmployeeEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author yandy
 * @description:
 * @date 2022/8/3 5:39 下午
 */
@Service
public class FlowMsgBuildCommonService extends BaseFlowMsgBuildHandleService {

    /**
     * 通用审批提醒消息标题 格式化
     */
    public String COMMON_REMIND_MSG_TITLE_FORMAT = "【%s】发起了一个%s，请及时处理";
    /**
     * 通用审批驳回标题 格式化
     */
    public String COMMON_REJECT_MSG_TITLE_FORMAT = "【%s】驳回了您的%s，请及时处理";
    /**
     * 通用审批通过标题 格式化
     */
    public String COMMON_PASS_MSG_TITLE_FORMAT = "【%s】通过了您的%s";
    /**
     * 通用审批评论消息 格式化
     */
    public String COMMON_COMMENT_MSG_TITLE_FORMAT = "【%s】评论了您提交的%s,请进入详情查看";

    @Autowired
    private EmployeeDao employeeDao;

    @Override
    public List<Integer> flowType() {
        return Lists.newArrayList();
    }

    @Override
    public String auditRemindMsg(FlowInstanceEntity flowInstanceEntity) {
        FlowTypeEnum flowTypeEnum = SmartBaseEnumUtil.getEnumByValue(flowInstanceEntity.getFlowType(), FlowTypeEnum.class);
        //发起人信息
        EmployeeEntity employeeEntity = employeeDao.selectById(flowInstanceEntity.getInitiatorId());
        if (employeeEntity == null) {
            return "";
        }
        String msg = String.format(COMMON_REMIND_MSG_TITLE_FORMAT, employeeEntity.getActualName(), flowTypeEnum.getDesc());
        return msg;
    }

    @Override
    public String auditResultMsg(FlowInstanceEntity flowInstanceEntity, Long auditorId, FlowAuditStatusEnum auditStatusEnum) {
        FlowTypeEnum flowTypeEnum = SmartBaseEnumUtil.getEnumByValue(flowInstanceEntity.getFlowType(), FlowTypeEnum.class);
        //审批人信息
        EmployeeEntity auditEmployeeEntity = employeeDao.selectById(auditorId);
        //发起人信息
        EmployeeEntity initiatorEmployeeEntity = employeeDao.selectById(flowInstanceEntity.getInitiatorId());
        if (auditEmployeeEntity == null || initiatorEmployeeEntity == null) {
            return "";
        }
        //消息标题
        String msg = "";
        if (FlowAuditStatusEnum.PASS == auditStatusEnum) {
            msg = String.format(COMMON_PASS_MSG_TITLE_FORMAT, auditEmployeeEntity.getActualName(), flowTypeEnum.getDesc());
        }
        if (FlowAuditStatusEnum.REJECT == auditStatusEnum) {
            msg = String.format(COMMON_REJECT_MSG_TITLE_FORMAT, initiatorEmployeeEntity.getActualName(), flowTypeEnum.getDesc());
        }
        return msg;
    }

    @Override
    public Object sendAuditCommentMsg(FlowInstanceEntity flowInstanceEntity, FlowCommentEntity flowCommentEntity, String operatorName) {
        FlowTypeEnum flowTypeEnum = SmartBaseEnumUtil.getEnumByValue(flowInstanceEntity.getFlowType(), FlowTypeEnum.class);
        // 构建消息
        String msg = String.format(COMMON_COMMENT_MSG_TITLE_FORMAT, operatorName, flowTypeEnum.getDesc());

        return msg;
    }
}