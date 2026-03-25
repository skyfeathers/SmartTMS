package net.lab1024.tms.common.module.business.mobileapp.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author yandy
 * @description:
 * @date 2023/5/16 11:16 下午
 */
@Data
public class MobileAppUpdateForm extends MobileAppAddForm{

    @ApiModelProperty("id")
    @NotNull(message = "id不能为空")
    private Long id;

}