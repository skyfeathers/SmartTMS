package net.lab1024.tms.admin.module.system.menu.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 菜单功能点操作Form
 *
 * @author 李善逸
 * @date 2021/7/30 20:56
 */
@Data
public class MenuTreeVO extends MenuVO{

    @ApiModelProperty("菜单子集")
    private List<MenuTreeVO> children;
}
