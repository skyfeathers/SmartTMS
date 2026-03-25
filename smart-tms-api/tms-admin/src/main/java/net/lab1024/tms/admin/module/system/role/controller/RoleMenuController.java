package net.lab1024.tms.admin.module.system.role.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.tms.admin.common.AdminBaseController;
import net.lab1024.tms.admin.constant.AdminSwaggerTagConst;
import net.lab1024.tms.admin.module.system.role.domain.form.RoleMenuUpdateForm;
import net.lab1024.tms.admin.module.system.role.domain.vo.RoleMenuTreeVO;
import net.lab1024.tms.admin.module.system.role.service.RoleMenuService;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartRequestEnterpriseUtil;
import net.lab1024.tms.common.module.support.operatelog.annoation.OperateLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 角色-菜单
 *
 * @author 李善逸
 */
@OperateLog
@RestController
@Api(tags = {AdminSwaggerTagConst.System.SYSTEM_ROLE_MENU})
public class RoleMenuController extends AdminBaseController {

    @Autowired
    private RoleMenuService roleMenuService;

    @ApiOperation("更新角色权限 @author zhuoda")
    @PostMapping("/role/menu/updateRoleMenu")
    @PreAuthorize("@saAuth.checkPermission('system:role:menu:update')")
    public ResponseDTO<String> updateRoleMenu(@Valid @RequestBody RoleMenuUpdateForm updateDTO) {
        return roleMenuService.updateRoleMenu(updateDTO);
    }

    @ApiOperation("获取角色关联菜单权限 @author zhuoda")
    @GetMapping("/role/menu/getRoleSelectedMenu/{roleId}")
    public ResponseDTO<RoleMenuTreeVO> getRoleSelectedMenu(@PathVariable Long roleId) {
        return roleMenuService.getRoleSelectedMenu(roleId, SmartRequestEnterpriseUtil.getRequestEnterpriseId());
    }
}
