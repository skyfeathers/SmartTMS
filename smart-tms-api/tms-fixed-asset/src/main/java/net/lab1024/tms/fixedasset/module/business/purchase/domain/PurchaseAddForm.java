package net.lab1024.tms.fixedasset.module.business.purchase.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.fixedasset.module.business.asset.domain.form.AssetAddForm;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 资产采购 - 新建
 *
 * @author lidoudou
 * @date 2023/3/20 上午10:32
 */
@Data
public class PurchaseAddForm {

    @ApiModelProperty(value = "所属公司",hidden = true)
    private Long enterpriseId;

    @ApiModelProperty("采购编号")
    private String purchaseNo;

    @ApiModelProperty("来源")
    @NotBlank(message = "来源不能为空")
    private String sourceId;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("资产列表")
    @Size(min = 1, message = "资产列表不能为空")
    @Valid
    private List<AssetAddForm> assetList;
}
