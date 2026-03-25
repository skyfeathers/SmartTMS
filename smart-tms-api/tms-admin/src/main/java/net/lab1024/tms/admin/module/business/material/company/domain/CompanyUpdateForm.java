package net.lab1024.tms.admin.module.business.material.company.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 业务资料-公司管理-编辑
 *
 * @author lihaifan
 * @date 2022/6/24 11:33
 */
@Data
public class CompanyUpdateForm extends CompanyCreateForm {

    @ApiModelProperty("公司ID")
    @NotNull(message = "公司ID不能为空")
    private Long companyId;
}
