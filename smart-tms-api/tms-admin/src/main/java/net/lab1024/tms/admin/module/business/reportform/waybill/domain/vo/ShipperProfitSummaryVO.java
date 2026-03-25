package net.lab1024.tms.admin.module.business.reportform.waybill.domain.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 货主毛利核算
 *
 * @author lidoudou
 * @date 2023/3/4 下午2:56
 */
@Data
public class ShipperProfitSummaryVO {

    @ApiModelProperty("应收总计")
    private BigDecimal receiveAmount;

    @ApiModelProperty("未开票金额")
    private BigDecimal waitInvoiceAmount;

    @ApiModelProperty("运单数量")
    private Integer waybillCount;

    @ApiModelProperty("应付总计")
    private BigDecimal payableAmount;

    @ApiModelProperty("司机工资")
    private BigDecimal salaryAmount;

    @ApiModelProperty("在途费用")
    private BigDecimal carCostAmount;

    @ApiModelProperty("税金")
    private BigDecimal taxAmount;

    @ApiModelProperty("利润")
    private BigDecimal profitAmount;

    @ApiModelProperty("毛利率")
    private BigDecimal profitRate;
}
