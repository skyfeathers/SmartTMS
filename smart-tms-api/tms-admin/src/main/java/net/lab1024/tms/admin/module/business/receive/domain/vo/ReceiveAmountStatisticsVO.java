package net.lab1024.tms.admin.module.business.receive.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 统计应收金额、已销金额、未销金额
 *
 * @author zhaoxinyang
 * @date 2023/8/31 11:34
 */
@Data
public class ReceiveAmountStatisticsVO {

    @ApiModelProperty("应收金额")
    private BigDecimal receivableTotalAmount;

    @ApiModelProperty("已销金额")
    private BigDecimal verificationTotalAmount;

    @ApiModelProperty("未销金额")
    private BigDecimal unpaidTotalAmount;

    @ApiModelProperty("逾期金额")
    private BigDecimal overdueTotalAmount;

}
