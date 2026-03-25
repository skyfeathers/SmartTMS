package net.lab1024.tms.admin.module.business.reportform.shipper.domain.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 货主日报表
 *
 * @author lidoudou
 * @date 2023/3/4 下午2:56
 */
@Data
public class ShipperDayStatisticVO {

    @ApiModelProperty("货主ID")
    private Long shipperId;

    @ApiModelProperty("货主名称")
    @Excel(name = "货主名称", width = 20)
    private String consignor;

    @ApiModelProperty("运单数量")
    @Excel(name = "下单数量", width = 10)
    private Integer waybillCount;

    @ApiModelProperty("异常运单数量")
    @Excel(name = "取消的订单数量", width = 15)
    private Integer cancelOrderCount;

    @ApiModelProperty("应收金额")
    @Excel(name = "应收金额", width = 15)
    private BigDecimal receiveAmount;

    @ApiModelProperty("运费金额")
    @Excel(name = "应付费用", width = 15)
    private BigDecimal freightAmount;

    @ApiModelProperty("异常金额")
    @Excel(name = "异常费用", width = 15)
    private BigDecimal exceptionAmount;

    @ApiModelProperty("司机工资")
    @Excel(name = "司机工资", width = 15)
    private BigDecimal salaryAmount;

    @ApiModelProperty("在途费用")
    @Excel(name = "在途费用", width = 15)
    private BigDecimal carCostAmount;

    @ApiModelProperty("税率")
    @Excel(name = "税率", width = 15)
    private BigDecimal taxRate;

    @ApiModelProperty("利润")
    @Excel(name = "毛利（税后）", width = 15)
    private BigDecimal profitAmount;

    @ApiModelProperty("去年同期运单数量")
    @Excel(name = "去年同期下单数量", width = 20)
    private Integer lastYearWaybillCount;

    @ApiModelProperty("去年同期下单金额")
    @Excel(name = "去年同期下单金额", width = 15)
    private BigDecimal lastYearWReceiveAmount;

    @ApiModelProperty("比例")
    @Excel(name = "比例", width = 10)
    private BigDecimal rate;

    @ApiModelProperty("应收账款金额")
    @Excel(name = "应收账款金额", width = 15)
    private BigDecimal receiveCostAmount;

    @ApiModelProperty("已开票金额")
    @Excel(name = "已开票金额", width = 15)
    private BigDecimal invoiceAmount;

    @ApiModelProperty("未开票金额")
    @Excel(name = "未开票金额", width = 15)
    private BigDecimal waitInvoiceAmount;

    @ApiModelProperty("未回收金额")
    @Excel(name = "未回收金额", width = 15)
    private BigDecimal unRecoveredAmount;

    @ApiModelProperty("所属公司ID")
    private Long enterpriseId;

    @ApiModelProperty("所属公司")
    @Excel(name = "所属公司", width = 25)
    private String enterpriseName;

    @ApiModelProperty("业务负责人ID")
    private Long managerId;

    @ApiModelProperty("业务负责人")
    @Excel(name = "所属销售", width = 15)
    private String managerName;

}
