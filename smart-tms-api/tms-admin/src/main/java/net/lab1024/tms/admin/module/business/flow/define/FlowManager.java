package net.lab1024.tms.admin.module.business.flow.define;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.lab1024.tms.admin.module.business.flow.define.domain.FlowEntity;
import net.lab1024.tms.admin.module.business.flow.define.domain.FlowTaskConfigForm;
import net.lab1024.tms.admin.module.business.flow.task.FlowTaskDao;
import net.lab1024.tms.admin.module.business.flow.task.domain.FlowTaskCreateForm;
import net.lab1024.tms.admin.module.business.flow.task.domain.FlowTaskEntity;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class FlowManager extends ServiceImpl<FlowDao, FlowEntity> {

    @Resource
    private FlowTaskDao flowTaskDao;


    @Transactional(rollbackFor = Throwable.class)
    public void handleSaveFlowTask(FlowTaskConfigForm configForm) {
        List<FlowTaskCreateForm> taskList = configForm.getTaskList();

        Long flowId = configForm.getFlowId();
        Long enterpriseId = configForm.getEnterpriseId();
        String parentIds = "0";
        for (FlowTaskCreateForm taskCreateForm : taskList) {
            FlowTaskEntity taskEntity = SmartBeanUtil.copy(taskCreateForm, FlowTaskEntity.class);
            taskEntity.setFlowId(flowId);
            taskEntity.setEnterpriseId(enterpriseId);
            taskEntity.setParentIds(parentIds);
            flowTaskDao.insert(taskEntity);
            parentIds = taskEntity.getTaskId().toString();
        }
    }

    /**
     * 修改审批流程
     *
     * @param configForm
     */
    @Transactional(rollbackFor = Throwable.class)
    public void handleUpdateFlow(FlowTaskConfigForm configForm) {
        // 修改审批流程
        Long flowId = configForm.getFlowId();
        // 删除原有审批流程任务
        flowTaskDao.deleteByFlowId(flowId, configForm.getEnterpriseId());

        // 保存新审批流程任务
        if(CollectionUtils.isNotEmpty(configForm.getTaskList())) {
            this.handleSaveFlowTask(configForm);
        }

    }


}
