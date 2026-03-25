package net.lab1024.tms.admin.module.business.shipper.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/***
 * 更新业务负责人
 * @author lidoudou
 * @date 2022/6/24 上午11:41
 */
@Data
public class ShipperManagerUpdateForm {

    @ApiModelProperty("货主ID")
    @NotNull(message = "货主不能为空")
    private List<Long> shipperIdList;

    @ApiModelProperty("业务负责人ID")
    @NotNull(message = "业务负责人不能为空")
    private List<Long> managerIdList;
}
