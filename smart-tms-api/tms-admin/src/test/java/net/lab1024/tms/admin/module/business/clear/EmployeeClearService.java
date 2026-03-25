package net.lab1024.tms.admin.module.business.clear;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import net.lab1024.tms.admin.module.business.reportform.employee.EmployeeSalesTargetDao;
import net.lab1024.tms.admin.module.system.employee.dao.EmployeeDao;
import net.lab1024.tms.admin.module.system.role.dao.RoleEmployeeDao;
import net.lab1024.tms.common.module.business.oa.enterprise.domain.EnterpriseEmployeeEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author lidoudou
 * @description:
 * @date 2023/5/30 上午10:08
 */
@Service
public class EmployeeClearService {
    @Autowired
    private EmployeeDao employeeDao;
    @Autowired
    private RoleEmployeeDao roleEmployeeDao;
    @Autowired
    private EmployeeSalesTargetDao employeeSalesTargetDao;

    public void clear(List<Long> notInEnterpriseIdList) {
        QueryWrapper qw = new QueryWrapper();
        qw.notIn("enterprise_id", notInEnterpriseIdList);

        employeeDao.delete(qw);
        roleEmployeeDao.delete(qw);
        employeeSalesTargetDao.delete(qw);
    }
}