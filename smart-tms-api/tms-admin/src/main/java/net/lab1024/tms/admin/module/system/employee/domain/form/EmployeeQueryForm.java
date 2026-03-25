package net.lab1024.tms.admin.module.system.employee.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.domain.PageParam;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Size;
import java.util.List;

/**
 * 员工列表
 *
 * @author 李开云
 */
@Data
public class EmployeeQueryForm extends PageParam {

    @ApiModelProperty("搜索词")
    @Length(max = 20, message = "搜索词最多20字符")
    private String keyword;

    @ApiModelProperty("部门id")
    private Long departmentId;

    @ApiModelProperty("部门id列表")
    private List<Long> departmentIdList;

    @ApiModelProperty("是否禁用")
    private Boolean disabledFlag;

    @ApiModelProperty("离职状态")
    private Boolean leaveFlag;

    @ApiModelProperty("员工id集合")
    @Size(max = 99, message = "最多查询99个员工")
    private List<Long> employeeIdList;

    @ApiModelProperty("角色ID")
    private Long roleId;

    @ApiModelProperty(value = "企业ID",hidden = true)
    private Long enterpriseId;


    @ApiModelProperty(value = "删除标识",hidden = true)
    private Boolean deletedFlag;

}
