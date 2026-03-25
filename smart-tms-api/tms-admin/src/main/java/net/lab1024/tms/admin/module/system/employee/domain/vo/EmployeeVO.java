package net.lab1024.tms.admin.module.system.employee.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.enumeration.GenderEnum;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;

import java.time.LocalDateTime;
import java.util.List;

/**
 * [  ]
 *
 * @author 罗伊
 * @date
 */
@Data
public class EmployeeVO {

    @ApiModelProperty("主键id")
    private Long employeeId;

    @ApiModelProperty("登录账号")
    private String loginName;

    @ApiModelPropertyEnum(GenderEnum.class)
    private Integer gender;

    @ApiModelProperty("员工名称")
    private String actualName;

    @ApiModelProperty("手机号码")
    private String phone;

    @ApiModelProperty("部门id")
    private Long departmentId;

    @ApiModelProperty("离职状态")
    private Boolean leaveFlag;

    @ApiModelProperty("是否被禁用")
    private Boolean disabledFlag;

    @ApiModelProperty("是否 超级管理员")
    private Boolean administratorFlag;

    @ApiModelProperty("部门名称")
    private String departmentName;

    @ApiModelProperty("企业Id")
    private Long enterpriseId;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("角色列表")
    private List<Long> roleIdList;

    @ApiModelProperty("角色名称列表")
    private List<String> roleNameList;
}
