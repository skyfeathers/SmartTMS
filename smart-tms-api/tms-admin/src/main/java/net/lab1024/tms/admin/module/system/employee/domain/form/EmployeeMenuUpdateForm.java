package net.lab1024.tms.admin.module.system.employee.domain.form;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;


/**
 * @author yandy
 */
@Data
public class EmployeeMenuUpdateForm {

    @ApiModelProperty("员工id")
    @NotNull(message = "员工id不能为空")
    private Long employeeId;

    @ApiModelProperty("菜单ID集合")
    private List<Long> menuIdList;

}
