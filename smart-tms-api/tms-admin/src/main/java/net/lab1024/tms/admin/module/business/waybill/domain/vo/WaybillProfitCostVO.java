package net.lab1024.tms.admin.module.business.waybill.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author zhuoda
 * @Date 2022-07-14
 */
@Data
public class WaybillProfitCostVO {

    @ApiModelProperty("费用项id")
    private Long costItemId;

    @ApiModelProperty("费用")
    private BigDecimal costAmount;

}
