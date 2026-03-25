package net.lab1024.tms.admin.module.business.pay.domain.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 导出应付账款明细
 *
 * @author lidoudou
 * @date 2022/9/16 下午4:40
 */
@Data
public class PayOrderExportVO {

    /**
     * 付款单id
     */
    private Long payOrderId;

    /**
     * 运单ID
     */
    private Long waybillId;

    @Excel(name = "序号", width = 10)
    private Integer index;

    @Excel(name = "申请单号", width = 20)
    private String payOrderNumber;

    @Excel(name = "运单编号", width = 20)
    private String waybillNumber;

    @Excel(name = "提箱时间", format = "yyyy-MM-dd HH:mm:ss", width = 20)
    private LocalDateTime deliverGoodsTime;

    @Excel(name = "箱号", width = 15)
    private String containerNumber;

    @Excel(name = "提交时间", format = "yyyy-MM-dd", width = 15)
    private LocalDateTime createTime;

    @Excel(name = "审核状态", width = 15)
    private String auditStatusDesc;

    @Excel(name = "当前审批人", width = 15)
    private String currentAuditor;

    @Excel(name = "所属公司", width = 20)
    private String enterpriseName;

    @Excel(name = "费用名称")
    private String costItemName;

    @Excel(name = "应收总金额", width = 15)
    private BigDecimal receiveAmount;

    @Excel(name = "本单税金")
    private BigDecimal taxAmount;

    @Excel(name = "本单利润")
    private BigDecimal profitAmount;

    @Excel(name = "付款状态", width = 15)
    private String payOrderStatusDesc;

    @Excel(name = "本次应付金额", width = 15, type = 10, numFormat = "0.00")
    private BigDecimal payableAmount;

//    @Excel(name = "实付金额", width = 15, type = 10, numFormat = "0.00")
//    private BigDecimal paidAmount;

    @Excel(name = "支付单位")
    private String settleTypeDesc;

    @Excel(name = "支付对象", width = 12)
    private String driverName;

//    @Excel(name = "开户名", width = 20)
//    private String accountName;
//
//    @Excel(name = "银行账号", width = 20)
//    private String bankAccount;
//
//    @Excel(name = "柜号", width = 12)
//    private String cabinetName;

    @Excel(name = "车牌", width = 15)
    private String vehicleNumber;

    /**
     * 付款单的备注
     */
    @Excel(name = "备注", width = 16)
    private String payOrderRemark;

    @Excel(name = "客户简称", width = 20)
    private String consignor;

    @Excel(name = "付款时间", format = "yyyy-MM-dd", width = 15)
    private LocalDateTime payTime;

    /**
     * 运单的备注
     */
    @Excel(name = "费用备注", width = 20)
    private String remark;

    @Excel(name = "账号名称", width = 20)
    private String payAccountName;

    @Excel(name = "经营方式", width = 12)
    private String businessModeDesc;

    @Excel(name = "提柜地点", width = 30)
    private String containerLocation;

    @Excel(name = "装货地点", width = 30)
    private String placingLocation;

    @Excel(name = "卸货地点", width = 30)
    private String unloadingLocation;

    @Excel(name = "还柜地点", width = 30)
    private String returnContainerLocation;

    @Excel(name = "客服")
    private String customerService;

    @Excel(name = "调度")
    private String scheduleName;

    @Excel(name = "装/卸货时间", format = "yyyy-MM-dd HH:mm:ss", width = 20)
    private LocalDateTime loadTime;

    @Excel(name = "最迟提箱时间", format = "yyyy-MM-dd HH:mm", width = 20)
    private LocalDateTime latestPackingTime;

    @Excel(name = "落重/还空时间", format = "yyyy-MM-dd HH:mm:ss", width = 20)
    private LocalDateTime receiveGoodsTime;

    @Excel(name = "运单创建时间", format = "yyyy-MM-dd HH:mm:ss", width = 20)
    private LocalDateTime waybillCreateTime;
}
