package net.lab1024.tms.admin.module.business.flow.define;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import net.lab1024.tms.admin.module.business.flow.config.FlowGatewayConfigService;
import net.lab1024.tms.admin.module.business.flow.config.FlowHandlerConfigService;
import net.lab1024.tms.admin.module.business.flow.constant.FlowTaskTypeEnum;
import net.lab1024.tms.admin.module.business.flow.define.domain.FlowTaskConfigForm;
import net.lab1024.tms.admin.module.business.flow.task.domain.FlowTaskCreateForm;
import net.lab1024.tms.common.common.constant.StringConst;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartBaseEnumUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FlowCheckService {

    @Resource
    private FlowHandlerConfigService flowHandlerConfigService;
    @Resource
    private FlowGatewayConfigService flowGatewayConfigService;


    /**
     * 检查流程任务配置
     *
     * @param taskList
     * @return
     */
    public ResponseDTO<String> checkFlowTaskConfig(List<FlowTaskCreateForm> taskList) {
        if (CollectionUtils.isEmpty(taskList)) {
            return ResponseDTO.ok();
        }
        String errorMsg = taskList.stream()
                .map(e -> this.checkFlowTaskConfig(e.getTaskType(), e.getTaskConfig()))
                .filter(e -> !e.getOk())
                .map(e -> e.getMsg())
                .collect(Collectors.joining(StringConst.SEPARATOR));
        if (StrUtil.isNotBlank(errorMsg)) {
            return ResponseDTO.userErrorParam(errorMsg);
        }

        return ResponseDTO.ok();
    }

    /**
     * 检查流程任务配置
     *
     * @param taskType
     * @param taskConfig
     * @return
     */
    private ResponseDTO<String> checkFlowTaskConfig(Integer taskType, String taskConfig) {
        FlowTaskTypeEnum taskTypeEnum = SmartBaseEnumUtil.getEnumByValue(taskType, FlowTaskTypeEnum.class);
        switch (taskTypeEnum) {
            case APPROVE:
            case CC: {
                ResponseDTO<String> resp = flowHandlerConfigService.checkFlowHandlerConfig(taskConfig);
                return resp;
            }
            case GATEWAY: {
                ResponseDTO<String> resp = flowGatewayConfigService.checkFlowGatewayConfig(taskConfig);
                return resp;
            }
            default:
                return ResponseDTO.ok();
        }
    }

}
