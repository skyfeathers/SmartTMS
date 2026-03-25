package net.lab1024.tms.admin.module.business.waybill.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author yandy
 * @description:
 * @date 2022/8/14 11:18 下午
 */
@Data
public class WaybillCostForm {

    @ApiModelProperty("运单id")
    @NotNull(message = "运单id不能为空")
    private Long waybillId;

    @ApiModelProperty("费用项")
    @NotEmpty(message = "费用项不能为空")
    @Valid
    private List<WaybillCostItemForm> costItemList;

    @ApiModelProperty("应收费用项")
    @NotEmpty(message = "应收费用项不能为空")
    @Valid
    private List<WaybillReceiveCostItemForm> receiveCostItemList;
}