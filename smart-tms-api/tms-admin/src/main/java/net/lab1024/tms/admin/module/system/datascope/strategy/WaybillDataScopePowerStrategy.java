package net.lab1024.tms.admin.module.system.datascope.strategy;

import net.lab1024.tms.admin.module.system.employee.dao.EmployeeDao;
import net.lab1024.tms.admin.module.system.role.dao.RoleEmployeeDao;
import net.lab1024.tms.admin.module.system.role.service.RoleService;
import net.lab1024.tms.common.module.support.systemconfig.SystemConfigKeyEnum;
import net.lab1024.tms.common.module.system.datascope.constant.DataScopeConstant;
import net.lab1024.tms.common.module.system.datascope.domain.DataScopeSqlConfig;
import net.lab1024.tms.common.module.system.datascope.service.DataScopeViewService;
import net.lab1024.tms.common.module.system.datascope.strategy.AbstractDataScopeStrategy;
import net.lab1024.tms.common.module.system.employee.domain.EmployeeEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


/**
 * 运单的数据范围区分
 * 包含客服角色，则根据运单创建人或者订单创建人筛选
 * 包含调度角色，则根据调度筛选
 * 两者都包含，根据调度 或 创建人筛选
 *
 * @author lidoudou
 * @date 2022/11/10 下午1:59
 */
@Component
public class WaybillDataScopePowerStrategy extends AbstractDataScopeStrategy {

    private static String JOIN_SQL = " (w.create_user_id in (#employeeIds) or o.create_user_id in (#employeeIds) or o.schedule_id in (#employeeIds))";

    /**
     * 客服的拼接sql
     */
    private static String CUSTOMER_SERVICE_JOIN_SQL = "(w.create_user_id in (#employeeIds) or o.create_user_id in (#employeeIds))";

    /**
     * 调度的拼接sql
     */
    private static String SCHEDULE_JOIN_SQL = " o.schedule_id in (#employeeIds)";

    @Autowired
    private EmployeeDao employeeDao;
    @Autowired
    private RoleService roleService;
    @Autowired
    private RoleEmployeeDao roleEmployeeDao;
    @Autowired
    private DataScopeViewService dataScopeViewService;

    @Override
    public String getCondition(Long enterpriseId, Long employeeId, Map<String, Object> paramMap, DataScopeSqlConfig sqlConfigDTO) {
        EmployeeEntity employeeEntity = employeeDao.selectById(employeeId);
        if (employeeEntity.getAdministratorFlag()) {
            return "";
        }

        List<Long> canViewEmployeeIds = dataScopeViewService.getCanViewEmployeeId(sqlConfigDTO.getDataScopeType(), employeeId);
        String employeeIds = StringUtils.join(canViewEmployeeIds, ",");
        if (CollectionUtils.isEmpty(canViewEmployeeIds)) {
            return "";
        }

        Long customerServiceRoleId = roleService.queryRoleId(enterpriseId, SystemConfigKeyEnum.CUSTOMER_SERVICE_ROLE_CODE);
        Long scheduleRoleId = roleService.queryRoleId(enterpriseId, SystemConfigKeyEnum.SCHEDULE_ROLE_CODE);
        List<Long> roleIdList = roleEmployeeDao.selectRoleIdByEmployeeId(employeeId);

        if (roleIdList.contains(customerServiceRoleId) && roleIdList.contains(scheduleRoleId)) {
            String sql = JOIN_SQL.replaceAll(DataScopeConstant.EMPLOYEE_PARAM, employeeIds);
            return sql;
        }

        if (roleIdList.contains(customerServiceRoleId)) {
            String sql = CUSTOMER_SERVICE_JOIN_SQL.replaceAll(DataScopeConstant.EMPLOYEE_PARAM, employeeIds);
            return sql;
        }

        if (roleIdList.contains(scheduleRoleId)) {
            String sql = SCHEDULE_JOIN_SQL.replaceAll(DataScopeConstant.EMPLOYEE_PARAM, employeeIds);
            return sql;
        }

        String sql = JOIN_SQL.replaceAll(DataScopeConstant.EMPLOYEE_PARAM, employeeIds);
        return sql;
    }
}