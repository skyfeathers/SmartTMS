package net.lab1024.tms.admin.module.system.employee.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.tms.admin.common.AdminBaseController;
import net.lab1024.tms.admin.constant.AdminSwaggerTagConst;
import net.lab1024.tms.admin.module.system.employee.domain.form.EmployeeMenuUpdateForm;
import net.lab1024.tms.admin.module.system.employee.domain.vo.EmployeeMenuTreeVO;
import net.lab1024.tms.admin.module.system.employee.service.EmployeeMenuService;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartRequestEnterpriseUtil;
import net.lab1024.tms.common.module.support.operatelog.annoation.OperateLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author yandy
 */
@OperateLog
@RestController
@Api(tags = {AdminSwaggerTagConst.System.SYSTEM_EMPLOYEE_MENU})
public class EmployeeMenuController extends AdminBaseController {

    @Autowired
    private EmployeeMenuService employeeMenuService;

    @ApiOperation("更新员工权限 @author yandy")
    @PostMapping("/employee/menu/updateEmployeeMenu")
    @PreAuthorize("@saAuth.checkPermission('system:enployee:menu:update')")
    public ResponseDTO<String> updateEmployeeMenu(@Valid @RequestBody EmployeeMenuUpdateForm updateDTO) {
        return employeeMenuService.updateEmployeeMenu(updateDTO);
    }

    @ApiOperation("获取员工关联菜单权限 @author yandy")
    @GetMapping("/employee/menu/getEmployeeSelectedMenu/{employeeId}")
    public ResponseDTO<EmployeeMenuTreeVO> getEmployeeSelectedMenu(@PathVariable Long employeeId) {
        return employeeMenuService.getEmployeeSelectedMenu(employeeId, SmartRequestEnterpriseUtil.getRequestEnterpriseId());
    }

    @ApiOperation("获取员工关联角色菜单权限 @author yandy")
    @GetMapping("/employee/menu/getEmployeeRoleMenu/{employeeId}")
    public ResponseDTO<EmployeeMenuTreeVO> getEmployeeRoleMenu(@PathVariable Long employeeId) {
        return employeeMenuService.getEmployeeRoleMenu(employeeId, SmartRequestEnterpriseUtil.getRequestEnterpriseId());
    }
}
