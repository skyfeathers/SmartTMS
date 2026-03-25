package net.lab1024.tms.admin.module.business.flow.msg;

import net.lab1024.tms.admin.module.business.flow.define.FlowDao;
import net.lab1024.tms.admin.module.business.flow.define.domain.FlowEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * [  ]
 *
 * @author yandanyang
 * @date 2021/10/15 14:33
 */
@Service
public class FlowMsgBuildService {

    @Autowired(required = false)
    private List<IFlowMsgBuildHandleService> flowMsgBuildHandleList;
    @Autowired
    private FlowMsgBuildCommonService flowMsgBuildCommonService;
    @Autowired
    private FlowDao flowDao;

    public IFlowMsgBuildHandleService getMsgBuildHandle(Long flowId) {
        FlowEntity flowEntity = flowDao.selectById(flowId);
        if (flowEntity == null) {
            return null;
        }
        if (CollectionUtils.isEmpty(flowMsgBuildHandleList)) {
            return null;
        }
        Integer flowType = flowEntity.getFlowType();
        Optional<IFlowMsgBuildHandleService> msgBuildHandleServiceOptional = flowMsgBuildHandleList.stream().filter(e -> e.flowType().contains(flowType)).findFirst();
        if (!msgBuildHandleServiceOptional.isPresent()) {
            return flowMsgBuildCommonService;
        }
        return msgBuildHandleServiceOptional.get();
    }
}
