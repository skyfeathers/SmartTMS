package net.lab1024.tms.admin.module.system.menu.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author zhuoda
 * @Date 2021/7/27
 */
@Data
public class MenuUpdateForm extends MenuBaseForm {

    @ApiModelProperty("菜单ID")
    @NotNull(message = "菜单ID不能为空")
    private Long menuId;

    @ApiModelProperty(hidden = true)
    private Long updateUserId;
}
