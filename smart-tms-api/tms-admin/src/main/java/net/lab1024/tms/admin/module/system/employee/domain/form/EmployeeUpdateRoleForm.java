package net.lab1024.tms.admin.module.system.employee.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * [  ]
 *
 * @author 罗伊
 * @date
 */
@Data
public class EmployeeUpdateRoleForm {

    @ApiModelProperty("员工id")
    @NotNull(message = "员工id不能为空")
    private Long employeeId;

    @ApiModelProperty("角色ids")
    @Size(max = 99, message = "角色最多99")
    private List<Long> roleIdList;

}
