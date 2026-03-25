package net.lab1024.tms.admin.module.system.role.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.tms.admin.common.AdminBaseController;
import net.lab1024.tms.admin.constant.AdminSwaggerTagConst;
import net.lab1024.tms.admin.module.system.role.domain.form.RoleAddForm;
import net.lab1024.tms.admin.module.system.role.domain.form.RoleUpdateForm;
import net.lab1024.tms.admin.module.system.role.domain.vo.RoleVO;
import net.lab1024.tms.admin.module.system.role.service.RoleService;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartRequestEnterpriseUtil;
import net.lab1024.tms.common.module.support.operatelog.annoation.OperateLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 角色管理路由
 *
 * @author 胡克
 */
@OperateLog
@RestController
@Api(tags = {AdminSwaggerTagConst.System.SYSTEM_ROLE})
public class RoleController extends AdminBaseController {

    @Autowired
    private RoleService roleService;

    @ApiOperation("添加角色")
    @PostMapping("/role/add")
    @PreAuthorize("@saAuth.checkPermission('system:role:add')")
    public ResponseDTO addRole(@Valid @RequestBody RoleAddForm roleAddForm) {
        roleAddForm.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return roleService.addRole(roleAddForm);
    }

    @ApiOperation("删除角色")
    @GetMapping("/role/delete/{roleId}")
    @PreAuthorize("@saAuth.checkPermission('system:role:delete')")
    public ResponseDTO<String> deleteRole(@PathVariable Long roleId) {
        return roleService.deleteRole(roleId);
    }

    @ApiOperation("更新角色")
    @PostMapping("/role/update")
    @PreAuthorize("@saAuth.checkPermission('system:role:update')")
    public ResponseDTO<String> updateRole(@Valid @RequestBody RoleUpdateForm roleUpdateDTO) {
        roleUpdateDTO.setEnterpriseId(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
        return roleService.updateRole(roleUpdateDTO);
    }

    @ApiOperation("获取角色数据")
    @GetMapping("/role/get/{roleId}")
    public ResponseDTO<RoleVO> getRole(@PathVariable("roleId") Long roleId) {
        return roleService.getRoleById(roleId);
    }

    @ApiOperation("获取所有角色")
    @GetMapping("/role/getAll")
    public ResponseDTO<List<RoleVO>> getAllRole() {
        return roleService.getAllRole(SmartRequestEnterpriseUtil.getRequestEnterpriseId());
    }

    @ApiOperation("获取所有角色")
    @GetMapping("/role/getByEnterpriseId/{enterpriseId}")
    public ResponseDTO<List<RoleVO>> getAllRole(@PathVariable Long enterpriseId) {
        return roleService.getAllRole(enterpriseId);
    }

}
