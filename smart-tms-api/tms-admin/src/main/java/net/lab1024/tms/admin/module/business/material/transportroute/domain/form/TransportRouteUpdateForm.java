package net.lab1024.tms.admin.module.business.material.transportroute.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 业务资料-运输路线
 *
 * @author lidoudou
 * @date 2022/7/12 下午3:05
 */
@Data
public class TransportRouteUpdateForm extends TransportRouteCreateForm {

    @ApiModelProperty("运输路线ID")
    @NotNull(message = "运输路线不能为空")
    private Long transportRouteId;
}
