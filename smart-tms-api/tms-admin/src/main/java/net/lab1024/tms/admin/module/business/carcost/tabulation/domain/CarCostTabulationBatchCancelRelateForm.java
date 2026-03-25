package net.lab1024.tms.admin.module.business.carcost.tabulation.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 批量关联运单
 *
 * @author zhaoxinyang
 * @date 2024/10/10 11:58
 */
@Data
public class CarCostTabulationBatchCancelRelateForm {

    @ApiModelProperty("费用ID列表")
    @NotNull(message = "费用ID列表不能为空")
    @Size(min = 1, message = "费用ID列表不能为空")
    private List<Long> tabulationIdList;

}
