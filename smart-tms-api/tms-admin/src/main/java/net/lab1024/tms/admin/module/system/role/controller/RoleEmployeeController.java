package net.lab1024.tms.admin.module.system.role.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.tms.admin.common.AdminBaseController;
import net.lab1024.tms.admin.constant.AdminSwaggerTagConst;
import net.lab1024.tms.admin.module.system.employee.domain.vo.EmployeeVO;
import net.lab1024.tms.admin.module.system.role.domain.form.RoleEmployeeQueryForm;
import net.lab1024.tms.admin.module.system.role.domain.form.RoleEmployeeUpdateForm;
import net.lab1024.tms.admin.module.system.role.domain.vo.RoleSelectedVO;
import net.lab1024.tms.admin.module.system.role.service.RoleEmployeeService;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.module.support.operatelog.annoation.OperateLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 用户角色管理路由
 *
 * @author 胡克
 */
@OperateLog
@RestController
@Api(tags = {AdminSwaggerTagConst.System.SYSTEM_ROLE_EMPLOYEE})
public class RoleEmployeeController extends AdminBaseController {

    @Autowired
    private RoleEmployeeService roleEmployeeService;

    @ApiOperation(value = "查询某个角色下的员工列表  @author zhuoda")
    @PostMapping("/role/employee/queryEmployee")
    public ResponseDTO<PageResult<EmployeeVO>> queryEmployee(@Valid @RequestBody RoleEmployeeQueryForm roleEmployeeQueryForm) {
        return roleEmployeeService.queryEmployee(roleEmployeeQueryForm);
    }

    @ApiOperation(value = "获取某个角色下的所有员工列表(无分页)  @author zhuoda")
    @GetMapping("/role/employee/getAllEmployeeByRoleId/{roleId}")
    public ResponseDTO<List<EmployeeVO>> listAllEmployeeRoleId(@PathVariable Long roleId) {
        return ResponseDTO.ok(roleEmployeeService.getAllEmployeeByRoleId(roleId));
    }

    @ApiOperation(value = "从角色成员列表中移除员工 @author zhuoda")
    @GetMapping("/role/employee/removeEmployee")
    @PreAuthorize("@saAuth.checkPermission('system:role:employee:delete')")
    public ResponseDTO<String> removeEmployee(Long employeeId, Long roleId) {
        return roleEmployeeService.removeRoleEmployee(employeeId, roleId);
    }

    @ApiOperation(value = "从角色成员列表中批量移除员工 @author zhuoda")
    @PostMapping("/role/employee/batchRemoveRoleEmployee")
    @PreAuthorize("@saAuth.checkPermission('system:role:employee:batch:delete')")
    public ResponseDTO<String> batchRemoveEmployee(@Valid @RequestBody RoleEmployeeUpdateForm updateForm) {
        return roleEmployeeService.batchRemoveRoleEmployee(updateForm);
    }

    @ApiOperation(value = "角色成员列表中批量添加员工 @author zhuoda")
    @PostMapping("/role/employee/batchAddRoleEmployee")
    @PreAuthorize("@saAuth.checkPermission('system:role:employee:add')")
    public ResponseDTO<String> addEmployeeList(@Valid @RequestBody RoleEmployeeUpdateForm addForm) {
        return roleEmployeeService.batchAddRoleEmployee(addForm);
    }

    @ApiOperation(value = "获取员工所有选中的角色和所有角色 @author zhuoda")
    @GetMapping("/role/employee/getRoles/{employeeId}")
    @PreAuthorize("@saAuth.checkPermission('system:role:employee:selected')")
    public ResponseDTO<List<RoleSelectedVO>> getRoleByEmployeeId(@PathVariable Long employeeId) {
        return ResponseDTO.ok(roleEmployeeService.getRoleInfoListByEmployeeId(employeeId));
    }
}
