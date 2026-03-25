package net.lab1024.tms.admin.module.system.dingding;

import net.lab1024.tms.admin.TmsAdminApplicationTest;
import net.lab1024.tms.common.module.support.dingding.config.DingDingConfigDao;
import net.lab1024.tms.common.module.support.dingding.config.domain.DingDingConfigEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

class DingDingDataInitServiceTest extends TmsAdminApplicationTest {

    @Autowired
    private EmployeeDingDingInitService employeeDingDingInitService;
    @Autowired
    private DepartmentDingDingInitService departmentDingDingInitService;
    @Autowired
    private DingDingConfigDao dingDingConfigDao;

    @Test
    void syncDingDepartment() {
        List<DingDingConfigEntity> configList = dingDingConfigDao.selectList(null);
        configList.forEach(item -> {
            departmentDingDingInitService.initDepartment(item.getEnterpriseId());
        });
    }

    @Test
    void syncDingEmployee() {
        List<DingDingConfigEntity> configList = dingDingConfigDao.selectList(null);
        configList.forEach(item -> {
            employeeDingDingInitService.initUser(item.getEnterpriseId());
        });
    }

}