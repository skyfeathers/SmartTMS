package net.lab1024.tms.admin.module.system.department.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * 
 * [  ]
 *
 * @author 罗伊
 * @date
 */
@Data
public class DepartmentAddForm {
    
    @ApiModelProperty("部门名称")
    @Length(min = 1, max = 50, message = "请输入正确的部门名称(1-50个字符)")
    @NotNull(message = "请输入正确的部门名称(1-50个字符)")
    private String name;

    @ApiModelProperty("排序")
    @NotNull(message = "排序值")
    private Integer sort;

    @ApiModelProperty("部门负责人id")
    private Long managerId;

    @ApiModelProperty("上级部门id (可选)")
    private Long parentId;

    @ApiModelProperty(value = "企业Id", hidden = true)
    private Long enterpriseId;

}
