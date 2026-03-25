package net.lab1024.tms.admin.module.system.department.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 部门层级树 查询
 *
 * @author listen
 * @date 2022年7月18日 17:23
 */
@Data
public class DepartmentTreeQueryForm {

    @ApiModelProperty("是否查询部门员工")
    private Boolean queryEmployeeFlag;
}
