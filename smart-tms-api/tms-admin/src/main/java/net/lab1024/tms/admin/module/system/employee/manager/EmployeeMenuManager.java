package net.lab1024.tms.admin.module.system.employee.manager;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.lab1024.tms.admin.module.system.employee.dao.EmployeeMenuDao;
import net.lab1024.tms.admin.module.system.employee.domain.entity.EmployeeMenuEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * @author yandy
 */
@Service
public class EmployeeMenuManager extends ServiceImpl<EmployeeMenuDao, EmployeeMenuEntity> {

    @Autowired
    private EmployeeMenuDao employeeMenuDao;

    /**
     * 更新员工权限
     * @param employeeId
     * @param employeeMenuEntityList
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateRoleMenu(Long employeeId, List<EmployeeMenuEntity> employeeMenuEntityList) {
        employeeMenuDao.deleteByEmployeeId(employeeId);
        // 批量添加菜单权限
        if (CollectionUtils.isNotEmpty(employeeMenuEntityList)) {
            saveBatch(employeeMenuEntityList);
        }
    }
}
