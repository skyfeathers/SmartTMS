package net.lab1024.tms.admin.module.system.menu.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zhuoda
 * @Date 2021/7/27
 */
@Data
public class MenuAddForm extends MenuBaseForm {

    @ApiModelProperty(hidden = true)
    private Long createUserId;
}
