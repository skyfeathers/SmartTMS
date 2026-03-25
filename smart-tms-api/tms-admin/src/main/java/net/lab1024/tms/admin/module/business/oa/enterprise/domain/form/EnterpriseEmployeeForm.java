package net.lab1024.tms.admin.module.business.oa.enterprise.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author yandy
 * @description:
 * @date 2022/8/21 3:21 下午
 */
@Data
public class EnterpriseEmployeeForm {

    @ApiModelProperty("企业id")
    @NotNull(message = "企业id不能为空")
    private Long enterpriseId;

    @ApiModelProperty("员工信息id")
    @NotEmpty(message = "员工信息id不能为空")
    private List<Long> employeeIdList;
}