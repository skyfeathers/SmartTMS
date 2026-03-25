package net.lab1024.tms.admin.module.business.receive.domain.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/***
 * 应收核销 导出
 *
 * @author lidoudou
 * @date 2022/10/19 下午4:15
 */
@Data
public class ReceiveOrderExportVO {

    @ApiModelProperty("收款单号")
    @Excel(name = "收款单号", width = 20)
    private String receiveOrderNumber;

    @ApiModelProperty("账期")
    @Excel(name = "账期", format = "yyyy-MM-dd", width = 20)
    private LocalDate accountPeriodDate;

    @ApiModelProperty("货主简称")
    @Excel(name = "货主简称", width = 20)
    private String shortName;

    @ApiModelProperty("是否需要开票")
    @Excel(name = "是否需要开票", width = 20, replace = {"是_true", "否_false"})
    private Boolean makeInvoiceFlag;

    @ApiModelProperty("开票状态")
    @Excel(name = "是否开票", width = 20, replace = {"待开票_1", "已开票_2", "部分开票_3", "已作废"})
    private Integer invoiceStatus;

    @ApiModelProperty("运费总额")
    @Excel(name = "运费总额", width = 20)
    private BigDecimal freight;

    @ApiModelProperty("异常费用总额")
    @Excel(name = "异常费用总额", orderNum = "4", width = 20)
    private BigDecimal abnormalAmount;

    @ApiModelProperty("应收合计")
    @Excel(name = "应收总额", width = 20)
    private BigDecimal totalAmount;

    @ApiModelProperty("已销金额")
    @Excel(name = "已销金额", width = 20)
    private BigDecimal verificationAmount;

    @ApiModelProperty("未销金额")
    @Excel(name = "未销金额", width = 20)
    private BigDecimal unpaidAmount;

    @ApiModelProperty("应收对账备注")
    @Excel(name = "应收对账备注", width = 20)
    private String remark;

    @ApiModelProperty("订单所属公司")
    @Excel(name = "订单所属公司", width = 20)
    private String enterpriseName;

    @Excel(name = "作废备注", width = 20)
    private String cancelRemark;

    @ApiModelProperty("创建时间")
    @Excel(name = "创建时间", format = "yyyy-MM-dd HH:mm:ss", width = 20)
    private LocalDateTime createTime;

    @ApiModelProperty("创建人")
    @Excel(name = "创建人", width = 15)
    private String createUserName;

}
