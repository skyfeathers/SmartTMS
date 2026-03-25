package net.lab1024.tms.fixedasset.module.business.consumables.purchase.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 易耗品采购主表
 *
 * @author lidoudou
 * @date 2023/4/12 下午4:02
 */
@Data
public class ConsumablesPurchaseAddForm {

    @ApiModelProperty("所属公司")
    private Long enterpriseId;

    @ApiModelProperty("所属位置")
    @NotNull(message = "所属位置不能为空")
    private Long locationId;

    @ApiModelProperty("来源")
    @NotNull(message = "采购来源不能为空")
    private String sourceId;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("资产列表")
    @NotNull(message = "请选择资产")
    @Valid
    private List<ConsumablesPurchaseItemAddForm> itemList;
}
