package net.lab1024.tms.admin.module.system.menu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.lab1024.tms.admin.module.system.menu.domain.entity.MenuEntity;
import net.lab1024.tms.admin.module.system.menu.domain.vo.MenuVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

/**
 * 菜单
 *
 * @author 李善逸
 */
@Mapper
@Component
public interface MenuDao extends BaseMapper<MenuEntity> {

    /**
     * 根据名称查询同一级下的菜单
     *
     * @param menuName   菜单名
     * @param parentId   父级id
     * @param deletedFlag 是否删除
     * @return
     */
    MenuEntity getByMenuName(@Param("menuName") String menuName, @Param("parentId") Long parentId, @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 根据前端权限字符串查询菜单
     *
     * @param permsIdentifier   前端权限字符串
     * @param deletedFlag 是否删除
     * @return
     */
    MenuEntity getByPermsIdentifier(@Param("permsIdentifier") String permsIdentifier, @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 根据菜单ID删除菜单（逻辑删除）
     *
     * @param menuIdList   菜单id集合
     * @param updateUserId 操作人id
     * @param deletedFlag   是否删除
     */
    void deleteByMenuIdList(@Param("menuIdList") List<Long> menuIdList, @Param("updateUserId") Long updateUserId, @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 查询菜单列表
     *
     * @param deletedFlag   是否删除
     * @param disabledFlag 是否禁用
     * @param menuTypeList 菜单类型集合
     * @return
     */
    List<MenuVO> queryMenuList(@Param("deletedFlag") Boolean deletedFlag, @Param("disabledFlag") Boolean disabledFlag, @Param("menuTypeList") List<Integer> menuTypeList, @Param("platformFlag") Boolean platformFlag);


    /**
     * 根据菜单ID 查询功能点列表
     *
     * @param menuId     菜单id
     * @param menuType   菜单类型
     * @param deletedFlag 删除标记
     * @return
     */
    List<MenuEntity> getPointListByMenuId(@Param("menuId") Long menuId, @Param("menuType") Integer menuType, @Param("deletedFlag") Boolean deletedFlag);

    /**
     * 根据员工ID查询菜单列表
     *
     * @param deletedFlag   是否删除
     * @param disabledFlag 禁用标识
     * @param employeeId   员工id
     * @return
     */
    List<MenuVO> queryMenuByEmployeeId(@Param("deletedFlag") Boolean deletedFlag,
                                       @Param("disabledFlag") Boolean disabledFlag,
                                       @Param("employeeId") Long employeeId);

    /**
     *
     * @param deletedFlag
     * @param disabledFlag
     * @param menuIdList
     * @return
     */
    List<MenuVO> queryByMenuIdList(@Param("deletedFlag") Boolean deletedFlag,@Param("disabledFlag") Boolean disabledFlag, @Param("menuIdList") Collection<Long> menuIdList, @Param("platformFlag") Boolean platformFlag);
}
