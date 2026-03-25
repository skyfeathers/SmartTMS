package net.lab1024.tms.admin.module.system.department.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.admin.module.system.employee.domain.vo.EmployeeVO;
import net.lab1024.tms.common.module.system.department.domain.DepartmentVO;

import java.util.List;

/**
 * @author 罗伊
 * @date 2021-01-30 23:57
 */
@Data
public class DepartmentEmployeeTreeVO extends DepartmentVO {

    @ApiModelProperty("部门员工列表")
    private List<EmployeeVO> employees;

    @ApiModelProperty("子部门")
    private List<DepartmentEmployeeTreeVO> children;

}
