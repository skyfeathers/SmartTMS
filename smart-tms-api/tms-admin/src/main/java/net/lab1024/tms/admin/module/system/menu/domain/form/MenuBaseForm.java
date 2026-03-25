package net.lab1024.tms.admin.module.system.menu.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.admin.module.system.menu.constant.MenuTypeEnum;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.common.validator.enumeration.CheckEnum;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 菜单基础
 *
 * @author 李善逸
 * @date 2021/7/29 23:16
 */
@Data
public class MenuBaseForm {

    @ApiModelProperty("菜单名称")
    @NotBlank(message = "菜单名称不能为空")
    @Length(max = 30, message = "菜单名称最多30个字符")
    private String menuName;

    @ApiModelPropertyEnum(value = MenuTypeEnum.class, desc = "类型")
    @CheckEnum(value = MenuTypeEnum.class, message = "类型错误")
    private Integer menuType;

    @ApiModelProperty("是否平台端菜单")
    private Boolean platformFlag;

    @ApiModelProperty("父菜单ID 无上级可传0")
    @NotNull(message = "父菜单ID不能为空")
    private Long parentId;

    @ApiModelProperty("显示顺序")
    private Integer sort;

    @ApiModelProperty("路由地址")
    private String path;

    @ApiModelProperty("组件路径")
    private String component;

    @ApiModelProperty("是否为外链")
    @NotNull(message = "是否为外链不能为空")
    private Boolean frameFlag;

    @ApiModelProperty("是否缓存")
    @NotNull(message = "是否缓存不能为空")
    private Boolean cacheFlag;

    @ApiModelProperty("显示状态")
    @NotNull(message = "显示状态不能为空")
    private Boolean visibleFlag;

    @ApiModelProperty("禁用状态")
    @NotNull(message = "禁用状态不能为空")
    private Boolean disabledFlag;

    @ApiModelProperty("权限字符串")
    private String permsIdentifier;

    @ApiModelProperty("接口权限")
    private String perms;


    @ApiModelProperty("接口权限（拆分）")
    private List<String> permsList;

    @ApiModelProperty("菜单图标")
    private String icon;

    @ApiModelProperty("功能点关联菜单ID")
    private Long contextMenuId;
}
