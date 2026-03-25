package net.lab1024.tms.admin.module.system.role.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import net.lab1024.tms.admin.module.system.department.dao.DepartmentDao;
import net.lab1024.tms.admin.module.system.employee.domain.vo.EmployeeVO;
import net.lab1024.tms.admin.module.system.role.dao.RoleDao;
import net.lab1024.tms.admin.module.system.role.dao.RoleEmployeeDao;
import net.lab1024.tms.common.module.system.role.domain.entity.RoleEmployeeEntity;
import net.lab1024.tms.common.module.system.role.domain.entity.RoleEntity;
import net.lab1024.tms.admin.module.system.role.domain.form.RoleEmployeeQueryForm;
import net.lab1024.tms.admin.module.system.role.domain.form.RoleEmployeeUpdateForm;
import net.lab1024.tms.admin.module.system.role.domain.vo.RoleSelectedVO;
import net.lab1024.tms.admin.module.system.role.manager.RoleEmployeeManager;
import net.lab1024.tms.common.common.code.UserErrorCode;
import net.lab1024.tms.common.common.constant.StringConst;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.common.util.SmartPageUtil;
import net.lab1024.tms.common.module.system.department.domain.DepartmentEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

/**
 * 角色管理业务
 *
 * @author zhuoda
 */
@Service
public class RoleEmployeeService {

    @Autowired
    private RoleEmployeeDao roleEmployeeDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private DepartmentDao departmentDao;
    @Autowired
    private RoleEmployeeManager roleEmployeeManager;

    /**
     * 通过角色id，分页获取成员员工列表
     *
     * @param roleEmployeeQueryForm
     * @return
     */
    public ResponseDTO<PageResult<EmployeeVO>> queryEmployee(RoleEmployeeQueryForm roleEmployeeQueryForm) {
        Page page = SmartPageUtil.convert2PageQuery(roleEmployeeQueryForm);
        List<EmployeeVO> employeeDTOS = roleEmployeeDao.selectRoleEmployeeByName(page, roleEmployeeQueryForm);
        if(CollectionUtils.isEmpty(employeeDTOS)){
            PageResult<EmployeeVO> PageResult = SmartPageUtil.convert2PageResult(page, Lists.newArrayList());
            return ResponseDTO.ok(PageResult);
        }
        //部门
        List<Long> departmentIdList = employeeDTOS.stream().filter(e -> e.getDepartmentId() != null).map(EmployeeVO::getDepartmentId).collect(Collectors.toList());
        Map<Long, String> departmentIdNameMap = Maps.newHashMap();
        if (CollectionUtils.isNotEmpty(departmentIdList)) {
            List<DepartmentEntity> departmentEntities = departmentDao.selectBatchIds(departmentIdList);
            departmentIdNameMap = departmentEntities.stream().collect(Collectors.toMap(DepartmentEntity::getDepartmentId, DepartmentEntity::getName));

        }
        // 企业员工
        List<Long> employeeIdList = employeeDTOS.stream().map(EmployeeVO::getEmployeeId).collect(Collectors.toList());
        //组装数据
        for (EmployeeVO employeeDTO : employeeDTOS) {
            employeeDTO.setDepartmentName(departmentIdNameMap.getOrDefault(employeeDTO.getDepartmentId(), StringConst.EMPTY));
        }


        PageResult<EmployeeVO> PageResult = SmartPageUtil.convert2PageResult(page, employeeDTOS, EmployeeVO.class);
        return ResponseDTO.ok(PageResult);
    }

    public List<EmployeeVO> getAllEmployeeByRoleId(Long roleId) {
        return roleEmployeeDao.selectEmployeeByRoleId(roleId);
    }

    /**
     * 移除员工角色
     *
     * @param employeeId
     * @param roleId
     * @return ResponseDTO<String>
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> removeRoleEmployee(Long employeeId, Long roleId) {
        if (null == employeeId || null == roleId) {
            return ResponseDTO.error(UserErrorCode.PARAM_ERROR);
        }
        roleEmployeeDao.deleteByEmployeeIdRoleId(employeeId, roleId);
        return ResponseDTO.ok();
    }

    /**
     * 批量删除角色的成员员工
     *
     * @param roleEmployeeUpdateForm
     * @return ResponseDTO<String>
     */
    public ResponseDTO<String> batchRemoveRoleEmployee(RoleEmployeeUpdateForm roleEmployeeUpdateForm) {
        roleEmployeeDao.batchDeleteEmployeeRole(roleEmployeeUpdateForm.getRoleId(), roleEmployeeUpdateForm.getEmployeeIdList());
        return ResponseDTO.ok();
    }

    /**
     * 批量添加角色的成员员工
     *
     * @param roleEmployeeUpdateForm
     * @return
     */
    public ResponseDTO<String> batchAddRoleEmployee(RoleEmployeeUpdateForm roleEmployeeUpdateForm) {
        Long roleId = roleEmployeeUpdateForm.getRoleId();
        List<Long> employeeIdList = roleEmployeeUpdateForm.getEmployeeIdList();

        List<EmployeeVO> roleEmployeeVOList = roleEmployeeDao.selectEmployeeByRoleId(roleId);
        List<Long> existsEmployeeIdList = roleEmployeeVOList.stream().map(EmployeeVO::getEmployeeId).collect(Collectors.toList());
        // 保存新的角色员工
        List<RoleEmployeeEntity> roleEmployeeList = null;
        if (CollectionUtils.isNotEmpty(employeeIdList)) {
            roleEmployeeList = employeeIdList.stream().filter(e -> !existsEmployeeIdList.contains(e))
                    .map(employeeId -> new RoleEmployeeEntity(roleId, employeeId))
                    .collect(Collectors.toList());
        }
        if (CollectionUtils.isEmpty(roleEmployeeList)) {
            return ResponseDTO.ok();
        }
        // 保存数据
        roleEmployeeManager.saveRoleEmployee(roleId, roleEmployeeList);
        return ResponseDTO.ok();
    }

    /**
     * 通过员工id获取员工角色
     *
     * @param employeeId
     * @return
     */
    public List<RoleSelectedVO> getRoleInfoListByEmployeeId(Long employeeId) {
        List<Long> roleIds = roleEmployeeDao.selectRoleIdByEmployeeId(employeeId);
        List<RoleEntity> roleList = roleDao.selectList(null);
        List<RoleSelectedVO> result = SmartBeanUtil.copyList(roleList, RoleSelectedVO.class);
        result.stream().forEach(item -> item.setSelected(roleIds.contains(item.getRoleId())));
        return result;
    }

    /**
     * 根据员工id 查询角色id集合
     *
     * @param employeeId
     * @return
     */
    public List<Long> getRoleIdList(Long employeeId) {
        return roleEmployeeDao.selectRoleIdByEmployeeId(employeeId);
    }


}
