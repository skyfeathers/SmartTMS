package net.lab1024.tms.admin.module.business.pay.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 统计应付的应付、已付合计
 *
 * @author lidoudou
 * @date 2022/9/23 下午3:56
 */
@Data
public class PayOrderAmountStatisticVO {

    @ApiModelProperty("应付金额")
    private BigDecimal payableTotalAmount;

    @ApiModelProperty("已付金额")
    private BigDecimal paidTotalAmount;
}
