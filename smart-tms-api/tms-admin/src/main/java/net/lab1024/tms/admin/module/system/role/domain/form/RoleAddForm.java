package net.lab1024.tms.admin.module.system.role.domain.form;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 角色添加DTO
 *
 * @author 胡克
 */
@Data
public class RoleAddForm {

    /**
     * 角色名称
     */
    @ApiModelProperty("角色名称")
    @NotBlank(message = "角色名称不能为空")
    @Length(min = 1, max = 20, message = "角色名称(1-20)个字符")
    private String roleName;

    @ApiModelProperty("角色编码")
    @NotBlank(message = "角色编码不能为空")
    private String roleCode;

    /**
     * 角色描述
     */
    @ApiModelProperty("角色描述")
    @Length(max = 255, message = "角色描述最多255个字符")
    private String remark;

    @ApiModelProperty(value = "企业Id", hidden = true)
    private Long enterpriseId;
}
