package net.lab1024.tms.admin.module.business.material.cabinet.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 业务资料-柜型管理-编辑
 *
 * @author lihaifan
 * @date 2022/6/24 11:33
 */
@Data
public class CabinetUpdateForm extends CabinetCreateForm {

    @ApiModelProperty("柜型ID")
    @NotNull(message = "柜型ID不能为空")
    private Long cabinetId;
}
