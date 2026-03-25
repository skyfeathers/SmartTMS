package net.lab1024.tms.admin.module.system.menu.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.admin.module.system.menu.domain.form.MenuBaseForm;

import java.time.LocalDateTime;

/**
 * @author zhuoda
 * @Date 2021/7/27
 */
@Data
public class MenuVO extends MenuBaseForm {

    @ApiModelProperty("菜单ID")
    private Long menuId;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("创建人")
    private Long createUserId;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty("更新人")
    private Long updateUserId;
}
