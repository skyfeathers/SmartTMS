package net.lab1024.tms.admin.module.system.menu.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.lab1024.tms.admin.common.AdminBaseController;
import net.lab1024.tms.admin.constant.AdminSwaggerTagConst;
import net.lab1024.tms.admin.module.system.menu.domain.form.MenuAddForm;
import net.lab1024.tms.admin.module.system.menu.domain.form.MenuUpdateForm;
import net.lab1024.tms.admin.module.system.menu.domain.vo.MenuTreeVO;
import net.lab1024.tms.admin.module.system.menu.domain.vo.MenuVO;
import net.lab1024.tms.admin.module.system.menu.service.MenuService;
import net.lab1024.tms.common.common.domain.RequestUrlVO;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartRequestUtil;
import net.lab1024.tms.common.module.support.operatelog.annoation.OperateLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 菜单
 *
 * @author zhuoda
 */
@OperateLog
@RestController
@Api(tags = {AdminSwaggerTagConst.System.SYSTEM_MENU})
public class MenuController extends AdminBaseController {

    @Autowired
    private MenuService menuService;

    @ApiOperation(value = "添加菜单 @author zhuoda")
    @PostMapping("/menu/add")
    @PreAuthorize("@saAuth.checkPermission('system:menu:add')")
    public ResponseDTO<String> addMenu(@RequestBody @Valid MenuAddForm menuAddForm) {
        menuAddForm.setCreateUserId(SmartRequestUtil.getRequestUserId());
        return menuService.addMenu(menuAddForm);
    }

    @ApiOperation(value = "更新菜单 @author zhuoda")
    @PostMapping("/menu/update")
    @PreAuthorize("@saAuth.checkPermission('system:menu:update')")
    public ResponseDTO<String> updateMenu(@RequestBody @Valid MenuUpdateForm menuUpdateForm) {
        menuUpdateForm.setUpdateUserId(SmartRequestUtil.getRequestUserId());
        return menuService.updateMenu(menuUpdateForm);
    }

    @ApiOperation(value = "批量删除菜单 @author zhuoda")
    @GetMapping("/menu/batchDelete")
    @PreAuthorize("@saAuth.checkPermission('system:menu:delete')")
    public ResponseDTO<String> batchDeleteMenu(@RequestParam("menuIdList") List<Long> menuIdList) {
        return menuService.batchDeleteMenu(menuIdList, SmartRequestUtil.getRequestUserId());
    }

    @ApiOperation(value = "查询菜单列表 @author zhuoda")
    @GetMapping("/menu/query")
    @PreAuthorize("@saAuth.checkPermission('system:menu:query')")
    public ResponseDTO<List<MenuVO>> queryMenuList() {
        return ResponseDTO.ok(menuService.queryMenuList(null));
    }

    @ApiOperation(value = "查询菜单详情 @author zhuoda")
    @GetMapping("/menu/detail/{menuId}")
    public ResponseDTO<MenuVO> getMenuDetail(@PathVariable Long menuId) {
        return menuService.getMenuDetail(menuId);
    }

    @ApiOperation(value = "查询菜单树 @author zhuoda")
    @GetMapping("/menu/tree")
    public ResponseDTO<List<MenuTreeVO>> queryMenuTree(@RequestParam("onlyMenu") Boolean onlyMenu) {
        return menuService.queryMenuTree(onlyMenu);
    }

    @ApiOperation(value = "获取所有请求路径 @author zhuoda")
    @GetMapping("/menu/auth/url")
    public ResponseDTO<List<RequestUrlVO>> getAuthUrl() {
        return menuService.getAuthUrl();
    }
}
