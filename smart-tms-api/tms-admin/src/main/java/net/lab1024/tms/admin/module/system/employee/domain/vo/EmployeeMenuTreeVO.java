package net.lab1024.tms.admin.module.system.employee.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.admin.module.system.menu.domain.vo.MenuSimpleTreeVO;

import java.util.List;


/**
 * @author yandy
 */
@Data
public class EmployeeMenuTreeVO {

    @ApiModelProperty("角员工ID")
    private Long employeeId;

    @ApiModelProperty("菜单列表")
    private List<MenuSimpleTreeVO> menuTreeList;

    @ApiModelProperty("选中的菜单ID")
    private List<Long> selectedMenuId;
}
