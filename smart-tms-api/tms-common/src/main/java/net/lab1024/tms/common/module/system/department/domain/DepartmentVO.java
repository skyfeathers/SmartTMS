package net.lab1024.tms.common.module.system.department.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 罗伊
 * @date 2021-01-30 23:57
 */
@Data
public class DepartmentVO {

    @ApiModelProperty("部门id")
    private Long departmentId;

    @ApiModelProperty("部门名称")
    private String name;

    @ApiModelProperty("部门负责人姓名")
    private String managerName;

    @ApiModelProperty("部门负责人id")
    private Long managerId;

    @ApiModelProperty("父级部门id")
    private Long parentId;

    @ApiModelProperty("企业id")
    private Long enterpriseId;

    @ApiModelProperty("排序")
    private Integer sort;

}
