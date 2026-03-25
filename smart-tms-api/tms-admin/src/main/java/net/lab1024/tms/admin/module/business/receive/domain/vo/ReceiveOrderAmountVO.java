package net.lab1024.tms.admin.module.business.receive.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 应收帐款 金额展示
 *
 * @author zhaoxinyang
 * @date 2023/9/13 15:53
 */
@Data
public class ReceiveOrderAmountVO {

    @ApiModelProperty("核销时间")
    private LocalDate verificationTime;

    @ApiModelProperty("核销备注")
    private String verificationRemark;

    @ApiModelProperty("收款单ID")
    private Long receiveOrderId;

    @ApiModelProperty("收款单号")
    private String receiveOrderNumber;

    @ApiModelProperty("货主ID")
    private Long shipperId;

    @ApiModelProperty("货主名称")
    private String consignor;

    @ApiModelProperty("负责人ID")
    private Long managerId;

    @ApiModelProperty("负责人名字")
    private String managerName;

    @ApiModelProperty("逾期天数")
    private Integer overdueDays;

    @ApiModelProperty("所属公司")
    private Long enterpriseId;

    @ApiModelProperty("订单所属公司")
    private String enterpriseName;

    @ApiModelProperty("业务日期，账单月份")
    private LocalDate businessDate;

    @ApiModelProperty("是否需要开票")
    private Boolean makeInvoiceFlag;

    @ApiModelProperty("应收合计")
    private BigDecimal totalAmount;

    @ApiModelProperty("已销金额")
    private BigDecimal verificationAmount;

    @ApiModelProperty("未销金额")
    private BigDecimal waitReceiveAmount;

    @ApiModelProperty("账期")
    private LocalDate accountPeriodDate;

    @ApiModelProperty("开票状态")
    private Integer invoiceStatus;

}
