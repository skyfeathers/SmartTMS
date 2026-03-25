package net.lab1024.tms.admin.module.system.employee.service;

import com.google.common.collect.Lists;
import net.lab1024.tms.admin.module.system.menu.dao.MenuDao;
import net.lab1024.tms.admin.module.system.menu.domain.vo.MenuVO;
import net.lab1024.tms.admin.module.system.role.service.RoleEmployeeService;
import net.lab1024.tms.admin.module.system.role.service.RoleMenuService;
import net.lab1024.tms.common.module.business.oa.enterprise.domain.EnterpriseEntity;
import net.lab1024.tms.common.module.system.employee.domain.EmployeeEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 员工权限校验
 *
 * @author 卓大
 */
@Service
public class EmployeePermissionService {

    @Autowired
    private RoleEmployeeService roleEmployeeService;

    @Autowired
    private RoleMenuService roleMenuService;
    @Autowired
    private EmployeeMenuService employeeMenuService;
    @Autowired
    private MenuDao menuDao;

    /**
     * 构建权限集合
     *
     * @param menuAndPointsList
     */
    public Set<? extends GrantedAuthority> buildAuthorities(List<MenuVO> menuAndPointsList) {
        HashSet<String> permissionList = new HashSet<>();
        for (MenuVO menu : menuAndPointsList) {
            if (StringUtils.isEmpty(menu.getPerms())) {
                continue;
            }
            //权限字符串
            if (StringUtils.isNotBlank(menu.getPermsIdentifier())) {
                permissionList.add(menu.getPermsIdentifier());
            }
            //接口权限
            String[] split = menu.getPerms().split(",");
            for (String perm : split) {
                permissionList.add(perm);
            }
        }

        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.addAll(permissionList.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet()));
        return authorities;
    }

    /**
     * 查询用户拥有的前端菜单项 用于登陆返回 前端动态路由配置
     *
     * @param enterpriseEntity
     * @param employeeEntity
     * @return
     */
    public List<MenuVO> getEmployeeMenuAndPointsList(EnterpriseEntity enterpriseEntity, EmployeeEntity employeeEntity) {
        Long employeeId = employeeEntity.getEmployeeId();
        Boolean administratorFlag = employeeEntity.getAdministratorFlag();
        if(administratorFlag && enterpriseEntity.getPlatformFlag()){
            return menuDao.queryByMenuIdList(false, false, Lists.newArrayList(), null);
        }
        if(administratorFlag && !enterpriseEntity.getPlatformFlag()){
            return menuDao.queryByMenuIdList(false, false, Lists.newArrayList(), false);
        }
        List<Long> roleIdList = roleEmployeeService.getRoleIdList(employeeId);
        //角色权限
        List<Long> roleMenuIdList = roleMenuService.getMenuIdList(roleIdList);
        //员工权限
        List<Long> employeeMenuIdList = employeeMenuService.getMenuIdList(employeeId);

        if(CollectionUtils.isEmpty(roleMenuIdList) && CollectionUtils.isEmpty(employeeMenuIdList)){
            return Lists.newArrayList();
        }
        // 以员工权限为主
        List<MenuVO> menuVOList = Lists.newArrayList();
        if(CollectionUtils.isNotEmpty(employeeMenuIdList)){
            menuVOList = menuDao.queryByMenuIdList(false, false, employeeMenuIdList, false);
        } else {
            menuVOList = menuDao.queryByMenuIdList(false, false, roleMenuIdList, false);
        }
        return menuVOList;
    }

}
