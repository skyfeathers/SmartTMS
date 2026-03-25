package net.lab1024.tms.admin.module.system.menu.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 简易的菜单VO
 *
 * @author 李善逸
 * @date 2021/7/30 22:41
 */
@Data
public class MenuSimpleTreeVO {

    @ApiModelProperty("菜单ID")
    private Long menuId;

    @ApiModelProperty("菜单名称")
    private String menuName;

    @ApiModelProperty("功能点关联菜单ID")
    private Long contextMenuId;

    @ApiModelProperty("父级菜单ID")
    private Long parentId;

    @ApiModelProperty("菜单类型")
    private Integer menuType;

    @ApiModelProperty("子菜单")
    private List<MenuSimpleTreeVO> children;
}
