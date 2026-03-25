package net.lab1024.tms.admin.module.system.department.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * [  ]
 *
 * @author 罗伊
 * @date
 */
@Data
public class DepartmentUpdateForm extends DepartmentAddForm {

    @ApiModelProperty("部门id")
    @NotNull(message = "部门id不能为空")
    private Long departmentId;

}
