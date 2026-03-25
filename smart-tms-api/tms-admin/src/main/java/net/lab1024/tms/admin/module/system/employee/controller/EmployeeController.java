package net.lab1024.tms.admin.module.system.employee.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.tms.admin.common.AdminBaseController;
import net.lab1024.tms.admin.constant.AdminSwaggerTagConst;
import net.lab1024.tms.admin.module.system.employee.domain.form.*;
import net.lab1024.tms.admin.module.system.employee.domain.vo.EmployeeVO;
import net.lab1024.tms.admin.module.system.employee.service.EmployeeService;
import net.lab1024.tms.common.common.domain.PageResult;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartRequestEnterpriseUtil;
import net.lab1024.tms.common.common.util.SmartRequestUtil;
import net.lab1024.tms.common.module.support.operatelog.annoation.OperateLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 员工管理
 *
 * @author zhuoda
 */
@RestController
@OperateLog
@Api(tags = {AdminSwaggerTagConst.System.SYSTEM_EMPLOYEE})
public class EmployeeController extends AdminBaseController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/employee/query")
    @ApiOperation(value = "员工管理查询 @author zhuoda")
    @PreAuthorize("@saAuth.checkPermission('system:employee:query')")
    public ResponseDTO<PageResult<EmployeeVO>> query(@Valid @RequestBody EmployeeQueryForm query) {
        query.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return employeeService.queryEmployee(query);
    }

    @ApiOperation(value = "添加员工 @author zhuoda")
    @PostMapping("/employee/add")
    @PreAuthorize("@saAuth.checkPermission('system:employee:add')")
    public ResponseDTO<String> addEmployee(@Valid @RequestBody EmployeeAddForm employeeAddForm) {
        employeeAddForm.setUpdateId(SmartRequestUtil.getRequestUserId());
        employeeAddForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return employeeService.addEmployee(employeeAddForm);
    }

    @ApiOperation(value = "更新员工 @author zhuoda")
    @PostMapping("/employee/update")
    @PreAuthorize("@saAuth.checkPermission('system:employee:update')")
    public ResponseDTO<String> updateEmployee(@Valid @RequestBody EmployeeUpdateForm employeeUpdateForm) {
        employeeUpdateForm.setUpdateId(SmartRequestUtil.getRequestUserId());
        employeeUpdateForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return employeeService.updateEmployee(employeeUpdateForm);
    }

    @ApiOperation(value = "更新员工禁用/启用状态 @author zhuoda")
    @GetMapping("/employee/update/disabled/{employeeId}")
    @PreAuthorize("@saAuth.checkPermission('system:employee:disabled')")
    public ResponseDTO<String> updateDisableFlag(@PathVariable Long employeeId) {
        return employeeService.updateDisableFlag(employeeId);
    }

    @ApiOperation(value = "批量删除员工 @author zhuoda")
    @PostMapping("/employee/update/batch/delete")
    @PreAuthorize("@saAuth.checkPermission('system:employee:delete')")
    public ResponseDTO<String> batchUpdateDeleteFlag(@RequestBody List<Long> employeeIdList) {
        return employeeService.batchUpdateDeleteFlag(employeeIdList);
    }

    @ApiOperation(value = "批量调整员工部门 @author zhuoda")
    @PostMapping("/employee/update/batch/department")
    @PreAuthorize("@saAuth.checkPermission('system:employee:department:update')")
    public ResponseDTO<String> batchUpdateDepartment(@Valid @RequestBody EmployeeBatchUpdateDepartmentForm batchUpdateDepartmentForm) {
        return employeeService.batchUpdateDepartment(batchUpdateDepartmentForm);
    }

    @ApiOperation(value = "修改密码 @author zhuoda")
    @PostMapping("/employee/update/password")
    public ResponseDTO<String> updatePassword(@Valid @RequestBody EmployeeUpdatePasswordForm updatePasswordForm) {
        updatePasswordForm.setEmployeeId(SmartRequestUtil.getRequestUserId());
        return employeeService.updatePassword(updatePasswordForm);
    }

    @ApiOperation(value = "重置员工密码 @author zhuoda")
    @GetMapping("/employee/update/password/reset/{employeeId}")
    @PreAuthorize("@saAuth.checkPermission('system:employee:password:reset')")
    public ResponseDTO<String> resetPassword(@PathVariable Integer employeeId) {
        return employeeService.resetPassword(employeeId);
    }

    @ApiOperation(value = "查询员工-根据部门id @author zhuoda")
    @GetMapping("/employee/getAllEmployeeByDepartmentId/{departmentId}")
    public ResponseDTO<List<EmployeeVO>> getAllEmployeeByDepartmentId(@PathVariable Long departmentId) {
        return employeeService.getAllEmployeeByDepartmentId(SmartRequestEnterpriseUtil.getRequestEnterpriseId(), departmentId, Boolean.FALSE);
    }

    @ApiOperation("查询所有员工 @author zhuoda")
    @GetMapping("/employee/queryAll")
    public ResponseDTO<List<EmployeeVO>> queryAllEmployee(@RequestParam(value = "disabledFlag", required = false) Boolean disabledFlag,
                                                          @RequestParam(value = "roleCode", required = false) String roleCode) {
        return employeeService.queryAllEmployee(SmartRequestEnterpriseUtil.getRequestEnterpriseId(), SmartRequestUtil.getRequestUserId(), disabledFlag, roleCode);
    }


}
