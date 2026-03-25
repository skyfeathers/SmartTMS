package net.lab1024.tms.admin.module.system.dingding;

import com.google.common.collect.Lists;
import net.lab1024.tms.common.module.support.dingding.callback.domain.dto.DingDingEventDeptDTO;
import net.lab1024.tms.common.module.support.dingding.callback.service.IDingCallBackHandleService;
import net.lab1024.tms.common.module.support.dingding.constants.DingDingEventTypeEnum;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author yandy
 * @description:
 * @date 2023/5/9 12:45 上午
 */
@Service
public class DepartmentDingDingRemoveEventHandleService implements IDingCallBackHandleService<DingDingEventDeptDTO> {

    @Override
    public List<String> registerEvent() {
        return Lists.newArrayList(DingDingEventTypeEnum.ORG_DEPT_REMOVE.getValue());
    }

    @Override
    public void handle(Long enterpriseId, DingDingEventDeptDTO dingEventDeptDTO) {
        return;
    }
}