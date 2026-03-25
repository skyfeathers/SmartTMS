package net.lab1024.tms.fixedasset.module.business.location.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 存放位置-编辑
 *
 * @author lidoudou
 * @date 2023/3/14 下午5:18
 */
@Data
public class LocationUpdateForm extends LocationCreateForm {

    @ApiModelProperty("位置ID")
    @NotNull(message = "位置ID不能为空")
    private Long locationId;
}
