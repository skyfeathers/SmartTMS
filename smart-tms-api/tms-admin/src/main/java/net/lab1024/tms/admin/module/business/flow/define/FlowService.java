package net.lab1024.tms.admin.module.business.flow.define;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import net.lab1024.tms.admin.module.business.flow.define.domain.*;
import net.lab1024.tms.admin.module.business.flow.listener.FlowTaskListener;
import net.lab1024.tms.admin.module.business.flow.listener.ITaskAuditListenerService;
import net.lab1024.tms.admin.module.business.flow.task.FlowTaskDao;
import net.lab1024.tms.admin.module.business.flow.task.domain.FlowTaskEntity;
import net.lab1024.tms.admin.module.business.flow.task.domain.FlowTaskListenerVO;
import net.lab1024.tms.admin.module.business.flow.task.domain.FlowTaskVO;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.common.util.SmartPageUtil;
import net.lab1024.tms.common.module.support.datatracer.domain.DataTracerRequestForm;
import net.lab1024.tms.common.module.support.operatelog.annoation.OperateLog;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FlowService {

    @Resource
    private FlowDao flowDao;
    @Resource
    private FlowTaskDao flowTaskDao;
    @Resource
    private FlowManager flowManager;
    @Resource
    private FlowCheckService flowCheckService;
    @Resource
    private ApplicationContext applicationContext;
    @Autowired(required = false)
    private List<ITaskAuditListenerService> taskAuditListenerServiceList;
    /**
     * 分页查询审批流程
     *
     * @return
     */
    public ResponseDTO<List<FlowVO>> flowQuery() {
        List<FlowVO> flowVOList = flowDao.selectList();
        return ResponseDTO.ok(flowVOList);
    }

    /**
     * 审批流程详情
     *
     * @param flowId
     * @return
     */
    public ResponseDTO<FlowVO> detail(Long flowId, Long enterpriseId) {
        FlowEntity flowEntity = flowDao.selectById(flowId);
        if (flowEntity == null) {
            return ResponseDTO.userErrorParam("审批流程不存在");
        }
        FlowVO flowVO = SmartBeanUtil.copy(flowEntity, FlowVO.class);
        this.buildTaskList(Lists.newArrayList(flowVO), enterpriseId);
        return ResponseDTO.ok(flowVO);
    }

    /**
     * 构建任务列表
     *
     * @param flowVOList
     */
    private void buildTaskList(List<FlowVO> flowVOList, Long enterpriseId) {
        if (CollUtil.isEmpty(flowVOList)) {
            return;
        }

        List<Long> flowIdList = flowVOList.stream().map(e -> e.getFlowId()).collect(Collectors.toList());
        List<FlowTaskEntity> allTaskList = flowTaskDao.selectByFlowIdList(flowIdList, enterpriseId);
        Map<Long, List<FlowTaskEntity>> taskMap =
                allTaskList.stream().collect(Collectors.groupingBy(e -> e.getFlowId()));

        flowVOList.forEach(e -> {
            List<FlowTaskEntity> taskList = taskMap.getOrDefault(e.getFlowId(), Lists.newArrayList());
            e.setTaskList(SmartBeanUtil.copyList(taskList, FlowTaskVO.class));
        });
    }

    /**
     * 审批流程监视器列表
     *
     * @return
     */
    public ResponseDTO<List<FlowTaskListenerVO>> listenerList() {
        if(CollectionUtils.isEmpty(taskAuditListenerServiceList)) {
            return ResponseDTO.ok(Lists.newArrayList());
        }
        List<FlowTaskListenerVO> list = Lists.newArrayList();
        taskAuditListenerServiceList.forEach(e->{
            FlowTaskListener flowTaskListener = AnnotationUtils.findAnnotation(e.getClass(), FlowTaskListener.class);
            if (flowTaskListener != null) {
                FlowTaskListenerVO flowTaskListenerVO = new FlowTaskListenerVO();
                flowTaskListenerVO.setName(flowTaskListener.value());
                flowTaskListenerVO.setDesc(flowTaskListener.desc());
                list.add(flowTaskListenerVO);
            }
        });
        return ResponseDTO.ok(list);
    }

    /**
     * 修改审批流程
     *
     * @param configForm
     * @param dataTracerRequestForm
     * @return
     */
    public ResponseDTO<String> flowTaskConfig(FlowTaskConfigForm configForm, DataTracerRequestForm dataTracerRequestForm) {
        FlowEntity flowEntity = flowDao.selectById(configForm.getFlowId());
        if (flowEntity == null) {
            return ResponseDTO.userErrorParam("审批流程不存在，不可修改");
        }
        ResponseDTO<String> resp = flowCheckService.checkFlowTaskConfig(configForm.getTaskList());
        if (!resp.getOk()) {
            return ResponseDTO.userErrorParam(resp.getMsg());
        }
        flowManager.handleUpdateFlow(configForm);
        return ResponseDTO.ok();
    }


}
