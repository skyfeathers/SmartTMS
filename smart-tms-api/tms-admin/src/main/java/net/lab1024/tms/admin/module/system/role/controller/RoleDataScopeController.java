package net.lab1024.tms.admin.module.system.role.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.tms.admin.constant.AdminSwaggerTagConst;
import net.lab1024.tms.admin.module.system.role.domain.form.RoleDataScopeUpdateForm;
import net.lab1024.tms.admin.module.system.role.domain.vo.RoleDataScopeVO;
import net.lab1024.tms.admin.module.system.role.service.RoleDataScopeService;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * 角色的数据权限配置
 *
 * @Author 1024创新实验室-主任: 卓大
 * @Date 2022-02-26 22:09:59
 * @Wechat zhuoda1024
 * @Email lab1024@163.com
 * @Copyright <a href="https://1024lab.net">1024创新实验室</a>
 */
@RestController
@Api(tags = {AdminSwaggerTagConst.System.SYSTEM_ROLE})
public class RoleDataScopeController {

    @Resource
    private RoleDataScopeService roleDataScopeService;

    @ApiOperation(value = "获取某角色所设置的数据范围 @author 卓大")
    @GetMapping("/role/dataScope/getRoleDataScopeList/{roleId}")
    public ResponseDTO<List<RoleDataScopeVO>> dataScopeListByRole(@PathVariable Long roleId) {
        return roleDataScopeService.getRoleDataScopeList(roleId);
    }

    @ApiOperation(value = "批量设置某角色数据范围 @author 卓大")
    @PostMapping("/role/dataScope/updateRoleDataScopeList")
    public ResponseDTO<String> updateRoleDataScopeList(@RequestBody @Valid RoleDataScopeUpdateForm roleDataScopeUpdateForm) {
        return roleDataScopeService.updateRoleDataScopeList(roleDataScopeUpdateForm);
    }


}
