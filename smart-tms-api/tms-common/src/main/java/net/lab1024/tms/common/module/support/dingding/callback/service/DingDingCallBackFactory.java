package net.lab1024.tms.common.module.support.dingding.callback.service;

import net.lab1024.tms.common.module.support.dingding.constants.DingDingEventTypeEnum;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author yandy
 * @description:
 * @date 2023/5/9 3:16 下午
 */
@Service
public class DingDingCallBackFactory {

    @Autowired(required = false)
    private List<IDingCallBackHandleService> dingCallBackHandleServiceList;

    public IDingCallBackHandleService getHandleService(DingDingEventTypeEnum eventTypeEnum){
        if (CollectionUtils.isEmpty(dingCallBackHandleServiceList)) {
            return null;
        }
        for (IDingCallBackHandleService dingCallBackHandleService : dingCallBackHandleServiceList) {
            List<String> registerEvent = dingCallBackHandleService.registerEvent();
            if (registerEvent.contains(eventTypeEnum.getValue())) {
                return dingCallBackHandleService;
            }
        }
        return  null;
    }
}