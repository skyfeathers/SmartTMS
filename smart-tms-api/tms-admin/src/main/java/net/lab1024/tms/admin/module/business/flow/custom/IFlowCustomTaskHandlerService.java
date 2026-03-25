package net.lab1024.tms.admin.module.business.flow.custom;

import java.util.List;

/**
 * [ 自定义审批处理人 ]
 *
 * @author yandanyang
 * @date 2021/9/15 16:00
 */
public interface IFlowCustomTaskHandlerService {

    /**
     * 自定义任务处理人
     * @param initiatorId 发起人id
     * @return
     */
    List<Long> customHandler(Long initiatorId, Long enterpriseId,Object businessData);
}
