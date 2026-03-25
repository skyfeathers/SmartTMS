package net.lab1024.tms.admin.module.business.oa.enterprise.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author yandy
 * @description:
 * @date 2025/2/6 10:43 上午
 */
@Data
public class EnterpriseWebsiteDescUpdateForm {

    @ApiModelProperty("企业ID")
    @NotNull(message = "企业ID不能为空")
    private Long enterpriseId;

    @ApiModelProperty("站点描述")
    @NotBlank(message = "站点描述不能为空")
    private String websiteDesc;
}