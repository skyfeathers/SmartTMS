package net.lab1024.tms.admin.module.business.waybill.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author lidoudou
 * @description:
 * @date 2023/7/26 下午3:29
 */
@Data
public class WaybillStatisticAmountAppVO extends WaybillStatisticAmountVO {

    @ApiModelProperty("应收金额")
    private BigDecimal currentMonthReceiveAmount;

    @ApiModelProperty("利润金额")
    private BigDecimal currentMonthProfitAmount;
}