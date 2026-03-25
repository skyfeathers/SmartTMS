package net.lab1024.tms.admin.module.system.employee.service;

import com.google.common.collect.Lists;
import net.lab1024.tms.admin.module.business.oa.enterprise.dao.EnterpriseDao;
import net.lab1024.tms.admin.module.system.employee.dao.EmployeeDao;
import net.lab1024.tms.admin.module.system.employee.dao.EmployeeMenuDao;
import net.lab1024.tms.admin.module.system.employee.domain.entity.EmployeeMenuEntity;
import net.lab1024.tms.admin.module.system.employee.domain.form.EmployeeMenuUpdateForm;
import net.lab1024.tms.admin.module.system.employee.domain.vo.EmployeeMenuTreeVO;
import net.lab1024.tms.admin.module.system.employee.manager.EmployeeMenuManager;
import net.lab1024.tms.admin.module.system.menu.dao.MenuDao;
import net.lab1024.tms.admin.module.system.menu.domain.vo.MenuSimpleTreeVO;
import net.lab1024.tms.admin.module.system.menu.domain.vo.MenuVO;
import net.lab1024.tms.admin.module.system.role.service.RoleEmployeeService;
import net.lab1024.tms.admin.module.system.role.service.RoleMenuService;
import net.lab1024.tms.common.common.code.UserErrorCode;
import net.lab1024.tms.common.common.domain.ResponseDTO;
import net.lab1024.tms.common.common.util.SmartBeanUtil;
import net.lab1024.tms.common.module.business.oa.enterprise.domain.EnterpriseEntity;
import net.lab1024.tms.common.module.system.employee.domain.EmployeeEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * @author yandy
 */
@Service
public class EmployeeMenuService {
    @Autowired
    private EnterpriseDao enterpriseDao;
    @Autowired
    private EmployeeDao employeeDao;
    @Autowired
    private EmployeeMenuDao employeeMenuDao;
    @Autowired
    private RoleEmployeeService roleEmployeeService;
    @Autowired
    private RoleMenuService roleMenuService;
    @Autowired
    private EmployeeMenuManager employeeMenuManager;
    @Autowired
    private MenuDao menuDao;

    /**
     * 更新权限
     *
     * @param updateForm
     * @return
     */
    public ResponseDTO<String> updateEmployeeMenu(EmployeeMenuUpdateForm updateForm) {
        Long employeeId = updateForm.getEmployeeId();
        EmployeeEntity employeeEntity = employeeDao.selectById(employeeId);
        if (null == employeeEntity) {
            return ResponseDTO.error(UserErrorCode.DATA_NOT_EXIST);
        }
        List<EmployeeMenuEntity> employeeMenuEntityList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(updateForm.getMenuIdList())) {
            for (Long menuId : updateForm.getMenuIdList()) {
                EmployeeMenuEntity employeeMenuEntity = new EmployeeMenuEntity();
                employeeMenuEntity.setEmployeeId(employeeId);
                employeeMenuEntity.setMenuId(menuId);
                employeeMenuEntityList.add(employeeMenuEntity);
            }
        }
        employeeMenuManager.updateRoleMenu(updateForm.getEmployeeId(), employeeMenuEntityList);
        return ResponseDTO.ok();
    }

    /**
     * 根据员工id集合，查询其所有的菜单权限
     *
     * @param employeeId
     * @return
     */
    public List<Long> getMenuIdList(Long employeeId) {
        return employeeMenuDao.queryMenuIdByEmployeeId(employeeId);
    }


    /**
     * 获取关联菜单权限
     *
     * @param employeeId
     * @return
     */
    public ResponseDTO<EmployeeMenuTreeVO> getEmployeeSelectedMenu(Long employeeId, Long enterpriseId) {
        // 企业信息
        EnterpriseEntity enterpriseEntity = enterpriseDao.selectById(enterpriseId);

        EmployeeMenuTreeVO menuTreeVO = new EmployeeMenuTreeVO();
        menuTreeVO.setEmployeeId(employeeId);
        //查询角色ID选择的菜单权限
        List<Long> selectedMenuId = employeeMenuDao.queryMenuIdByEmployeeId(employeeId);
        menuTreeVO.setSelectedMenuId(selectedMenuId);
        //查询菜单权限
        List<MenuVO> menuVOList = Lists.newArrayList();
        if(enterpriseEntity.getPlatformFlag()){
            menuVOList = menuDao.queryByMenuIdList(false, false, null, null);
        } else {
            menuVOList = menuDao.queryByMenuIdList(false, false, null, false);
        }
        Map<Long, List<MenuVO>> parentMap = menuVOList.stream().collect(Collectors.groupingBy(MenuVO::getParentId, Collectors.toList()));
        List<MenuSimpleTreeVO> menuTreeList = this.buildMenuTree(parentMap, NumberUtils.LONG_ZERO);
        menuTreeVO.setMenuTreeList(menuTreeList);
        return ResponseDTO.ok(menuTreeVO);
    }

    /**
     * 获取关联角色菜单权限
     *
     * @param employeeId
     * @return
     */
    public ResponseDTO<EmployeeMenuTreeVO> getEmployeeRoleMenu(Long employeeId, Long enterpriseId) {
        // 企业信息
        EnterpriseEntity enterpriseEntity = enterpriseDao.selectById(enterpriseId);

        EmployeeMenuTreeVO menuTreeVO = new EmployeeMenuTreeVO();
        menuTreeVO.setEmployeeId(employeeId);
        //查询角色ID选择的菜单权限
        List<Long> roleIdList = roleEmployeeService.getRoleIdList(employeeId);
        List<Long> selectedMenuId = roleMenuService.getMenuIdList(roleIdList);
        menuTreeVO.setSelectedMenuId(selectedMenuId);
        //查询菜单权限
        List<MenuVO> menuVOList = Lists.newArrayList();
        if(enterpriseEntity.getPlatformFlag()){
            menuVOList = menuDao.queryByMenuIdList(false, false, null, null);
        } else {
            menuVOList = menuDao.queryByMenuIdList(false, false, null, false);
        }
        Map<Long, List<MenuVO>> parentMap = menuVOList.stream().collect(Collectors.groupingBy(MenuVO::getParentId, Collectors.toList()));
        List<MenuSimpleTreeVO> menuTreeList = this.buildMenuTree(parentMap, NumberUtils.LONG_ZERO);
        menuTreeVO.setMenuTreeList(menuTreeList);
        return ResponseDTO.ok(menuTreeVO);
    }

    /**
     * 构建菜单树
     *
     * @return
     */
    private List<MenuSimpleTreeVO> buildMenuTree(Map<Long, List<MenuVO>> parentMap, Long parentId) {
        // 获取本级菜单树List
        List<MenuSimpleTreeVO> res = parentMap.getOrDefault(parentId, Lists.newArrayList()).stream()
                .map(e -> SmartBeanUtil.copy(e, MenuSimpleTreeVO.class)).collect(Collectors.toList());
        // 循环遍历下级菜单
        res.forEach(e -> {
            e.setChildren(this.buildMenuTree(parentMap, e.getMenuId()));
        });
        return res;
    }
}
