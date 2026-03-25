package net.lab1024.tms.admin.module.system.menu.domain.bo;

import lombok.Data;
import net.lab1024.tms.admin.module.system.menu.domain.vo.MenuTreeVO;
import net.lab1024.tms.admin.module.system.menu.domain.vo.MenuVO;

import java.util.List;

/**
 * 登陆时需要的菜单BO
 *
 * @author 李善逸
 * @date 2021/8/5 21:35
 */
@Data
public class MenuLoginBO {

    /**
     * 菜单树
     */
    private List<MenuTreeVO> menuTree;

    /**
     * 功能点权限列表
     */
    private List<String> pointsList;

    /**
     * 菜单列表
     */
    private List<MenuVO> menuList;

    /**
     * 全部菜单列表
     */
    private List<MenuVO> allMenuList;

}
