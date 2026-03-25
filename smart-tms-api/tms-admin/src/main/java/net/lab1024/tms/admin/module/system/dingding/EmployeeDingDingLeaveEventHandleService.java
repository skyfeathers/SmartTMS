package net.lab1024.tms.admin.module.system.dingding;

import com.google.common.collect.Lists;
import net.lab1024.tms.admin.module.system.employee.manager.EmployeeManager;
import net.lab1024.tms.common.module.support.dingding.callback.domain.dto.DingDingEventUserDTO;
import net.lab1024.tms.common.module.support.dingding.callback.service.IDingCallBackHandleService;
import net.lab1024.tms.common.module.support.dingding.constants.DingDingEventTypeEnum;
import net.lab1024.tms.common.module.support.dingding.sync.dao.DingDingEmployeeDao;
import net.lab1024.tms.common.module.support.dingding.sync.domain.DingDingEmployeeEntity;
import net.lab1024.tms.common.module.system.employee.domain.EmployeeEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author yandy
 * @description:
 * @date 2023/5/9 12:45 上午
 */
@Service
public class EmployeeDingDingLeaveEventHandleService implements IDingCallBackHandleService<DingDingEventUserDTO> {
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private DingDingEmployeeDao dingDingEmployeeDao;

    @Override
    public List<String> registerEvent() {
        return Lists.newArrayList(DingDingEventTypeEnum.USER_LEAVE_ORG.getValue());
    }

    @Override
    public void handle(Long enterpriseId, DingDingEventUserDTO dingEvent) {
        List<String> userIdList = dingEvent.getUserId();
        if(CollectionUtils.isEmpty(userIdList)) {
            return;
        }
        List<DingDingEmployeeEntity> dingDingEmployeeEntityList = dingDingEmployeeDao.selectByUserIdList(userIdList);
        if (CollectionUtils.isEmpty(dingDingEmployeeEntityList)) {
            return;
        }
        List<EmployeeEntity> updateEntityList = Lists.newArrayList();
        for (DingDingEmployeeEntity dingDingEmployeeEntity : dingDingEmployeeEntityList) {
            EmployeeEntity updateEntity = new EmployeeEntity();
            updateEntity.setEmployeeId(dingDingEmployeeEntity.getEmployeeId());
            updateEntity.setLeaveFlag(Boolean.TRUE);
            updateEntity.setDisabledFlag(Boolean.TRUE);
            updateEntityList.add(updateEntity);
        }
        employeeManager.updateBatchById(updateEntityList);
    }
}