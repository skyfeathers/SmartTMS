package net.lab1024.tms.fixedasset.module.business.check.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 盘点
 *
 * @author lidoudou
 * @date 2023/3/24 下午4:42
 */
@Data
public class CheckForm {

    @NotNull(message = "盘点ID不能为空")
    private Long checkId;

    @ApiModelProperty("资产列表")
    @Size(min = 1, message = "资产列表不能为空")
    @Valid
    private List<CheckAssetForm> assetList;
}
