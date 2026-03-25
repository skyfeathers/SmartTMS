package net.lab1024.tms.admin.module.business.capitalflow.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 资金流水统计
 *
 * @author lidoudou
 * @date 2022/8/20 下午5:33
 */
@Data
public class CapitalFlowStatisticVO {

    @ApiModelProperty("应收的总金额")
    private BigDecimal receiveAmount;

    @ApiModelProperty("应付款总金额")
    private BigDecimal payAmount;
}
