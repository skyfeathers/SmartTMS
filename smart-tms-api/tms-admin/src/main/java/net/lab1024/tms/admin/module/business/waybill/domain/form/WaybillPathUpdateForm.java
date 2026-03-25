package net.lab1024.tms.admin.module.business.waybill.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author yandy
 * @description:
 * @date 2022/12/15 5:03 下午
 */
@Data
public class WaybillPathUpdateForm {

    @ApiModelProperty("运单ID")
    @NotNull(message = "运单ID不能为空")
    private Long waybillId;

    @ApiModelProperty("运输距离单位公里")
    @NotNull(message = "运输距离不能为空")
    private BigDecimal distance;

    @ApiModelProperty("运输路线")
    @NotNull(message = "运输路线不能为空")
    private List<WaybillPathForm> pathList;
}