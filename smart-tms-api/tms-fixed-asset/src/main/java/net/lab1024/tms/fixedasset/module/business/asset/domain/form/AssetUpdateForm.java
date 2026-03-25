package net.lab1024.tms.fixedasset.module.business.asset.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 固定资产 更新表单
 *
 * @Author lidoudou
 * @Date 2023-03-15 14:15:14
 * @Copyright 1024创新实验室
 */

@Data
public class AssetUpdateForm extends AssetAddForm {

    @ApiModelProperty(value = "资产ID", required = true)
    @NotNull(message = "资产ID 不能为空")
    private Long assetId;

}