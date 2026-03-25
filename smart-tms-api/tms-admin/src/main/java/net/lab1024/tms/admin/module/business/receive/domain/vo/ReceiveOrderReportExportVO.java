package net.lab1024.tms.admin.module.business.receive.domain.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 应收帐款导出
 *
 * @author lidoudou
 * @date 2022/12/3 下午2:11
 */
@Data
public class ReceiveOrderReportExportVO {

    @ApiModelProperty("应收ID")
    private Long receiveOrderId;

    @ApiModelProperty("货主ID")
    private Long shipperId;

    @ApiModelProperty("公司ID")
    private Long enterpriseId;

    @Excel(name = "地区", width = 15)
    private String areaName;

    @Excel(name = "公司", width = 30)
    private String enterpriseName;

//    @Excel(name = "收款单号")
//    private String receiveOrderNumber;

    @Excel(name = "是否需要开票", width = 30, replace = {"是_true", "否_false"})
    private Boolean makeInvoiceFlag;

//    @Excel(name = "货主", width = 30)
//    private String consignor;

    @Excel(name = "开票单位/货主", width = 30)
    private String invoiceName;

    @Excel(name = "业务日期", format = "yyyy年MM月", width = 15)
    private LocalDate businessDate;

    @Excel(name = "开票日期", format = "yyyy-MM-dd", width = 20)
    private LocalDate invoiceTime;

    @Excel(name = "开票/应收金额", width = 30)
    private BigDecimal totalAmount;

    @Excel(name = "回款金额")
    private BigDecimal verificationAmount;

    @Excel(name = "应收款余额", width = 15)
    private BigDecimal waitReceiveAmount;

    @Excel(name = "应收帐款到期日", format = "yyyy-MM-dd", width = 30)
    private LocalDate accountPeriodDate;

    @Excel(name = "回款日期", format = "yyyy-MM-dd")
    private LocalDateTime verificationTime;

    @Excel(name = "逾期天数")
    private Integer overdueDays;

    @Excel(name = "业务人员")
    private String managerName;

    @Excel(name = "发票号")
    private String invoiceNumber;

    @Excel(name = "备注")
    private String verificationRemark;
}
