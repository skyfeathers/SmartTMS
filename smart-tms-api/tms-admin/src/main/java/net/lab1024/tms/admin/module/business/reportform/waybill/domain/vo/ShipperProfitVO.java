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
public class ShipperProfitVO {

    @ApiModelProperty("货主ID")
    private Long shipperId;

    @ApiModelProperty("名称")
    @Excel(name = "货主名称", orderNum = "2")
    private String consignor;

    @ApiModelProperty("业务负责人")
    @Excel(name = "所属销售", orderNum = "15")
    private String managerName;

    @ApiModelProperty("所属公司ID")
    private Long enterpriseId;

    @ApiModelProperty("所属公司")
    @Excel(name = "所属公司", orderNum = "1")
    private String enterpriseName;

    @ApiModelProperty("外调车应收")
    @Excel(name = "外派车应收", orderNum = "5")
    private BigDecimal arrivePayReceiveAmount;

    @ApiModelProperty("外调车应付")
    @Excel(name = "外派车应付", orderNum = "10")
    private BigDecimal arrivePayAmount;

    @ApiModelProperty("挂靠车月结应收")
    @Excel(name = "挂靠车应收", orderNum = "4")
    private BigDecimal monthReceiveAmount;

    @ApiModelProperty("挂靠车月结应付")
    @Excel(name = "挂靠车应付", orderNum = "9")
    private BigDecimal monthPayAmount;

    @ApiModelProperty("自有车应收")
    @Excel(name = "自有车应收", orderNum = "3")
    private BigDecimal selfVehicleReceiveAmount;

    @ApiModelProperty("自有车应付")
    @Excel(name = "自有车应付", orderNum = "8")
    private BigDecimal selfVehiclePayAmount;

    @ApiModelProperty("应收总计")
    @Excel(name = "应收金额总计", orderNum = "6")
    private BigDecimal receiveAmount;

    @ApiModelProperty("应付总计")
    @Excel(name = "应付总计", orderNum = "11")
    private BigDecimal payableAmount;

    @ApiModelProperty("未开票金额")
    @Excel(name = "未开票金额", orderNum = "11")
    private BigDecimal waitInvoiceAmount;

    @ApiModelProperty("已开票金额")
    @Excel(name = "已开票金额", orderNum = "11")
    private BigDecimal invoiceAmount;

    @ApiModelProperty("自有车单量")
    @Excel(name = "自有车单量", orderNum = "7")
    private Integer selfVehicleWaybillCount;

    @ApiModelProperty("外调车运单量")
    @Excel(name = "外派车单量", orderNum = "7")
    private Integer arrivePayWaybillCount;

    @ApiModelProperty("挂靠车单量")
    @Excel(name = "挂靠车单量", orderNum = "7")
    private Integer monthWaybillCount;

    @ApiModelProperty("运单数量")
    @Excel(name = "总运单量", orderNum = "7")
    private Integer waybillCount;

    @ApiModelProperty("司机工资")
    @Excel(name = "司机工资", orderNum = "10")
    private BigDecimal salaryAmount;

    @ApiModelProperty("在途费用")
    @Excel(name = "在途费用", orderNum = "11")
    private BigDecimal carCostAmount;

    @ApiModelProperty("税金")
    @Excel(name = "税金", orderNum = "12")
    private BigDecimal taxAmount;

    @ApiModelProperty("利润")
    @Excel(name = "毛利润", orderNum = "13")
    private BigDecimal profitAmount;

    @ApiModelProperty("毛利率")
    @Excel(name = "毛利率", orderNum = "14")
    private BigDecimal profitRate;
}
