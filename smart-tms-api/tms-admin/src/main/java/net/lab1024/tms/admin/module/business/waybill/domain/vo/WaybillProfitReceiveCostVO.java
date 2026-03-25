package net.lab1024.tms.admin.module.business.waybill.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author yandy
 * @description:
 * @date 2022/9/29 4:13 下午
 */
@Data
public class WaybillProfitReceiveCostVO {

    @ApiModelProperty("费用项id")
    private Long costItemId;

    @ApiModelProperty("费用")
    private BigDecimal costAmount;
}