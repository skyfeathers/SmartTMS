package net.lab1024.tms.admin.module.business.waybill.domain.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author zhuoda
 * @Date 2022-07-14
 */
@Data
public class WaybillExportVO {

    @Excel(name = "运单号", width = 20)
    private String waybillNumber;

    @Excel(name = "订单号", width = 20)
    private String orderNo;

    @Excel(name = "客户订单号", width = 20)
    private String shipperOrderNumber;

    @Excel(name = "箱号", width = 20)
    private String containerNumber;

    @Excel(name = "铅封号", width = 20)
    private String leadSealNumber;

    @Excel(name = "柜型名称")
    private String cabinetName;

    @Excel(name = "订单最迟提箱时间", format = "yyyy-MM-dd HH:mm", width = 20)
    private LocalDateTime latestPackingTime;

    @Excel(name = "货主", width = 20)
    private String shortName;

    @Excel(name = "业务时间", width = 10, format = "yyyy-MM")
    private LocalDate businessDate;

    @Excel(name = "结算方式", width = 20)
    private String settleModeDesc;

    @Excel(name = "结算类型")
    private String settleTypeDesc;

    @Excel(name = "司机")
    private String driverName;

    @Excel(name = "车辆", width = 20)
    private String vehicleNumber;

    @Excel(name = "车队", width = 20)
    private String fleetName;

    @Excel(name = "应收金额")
    private BigDecimal receiveAmount;

    @Excel(name = "应付金额")
    private BigDecimal payableAmount;

    @Excel(name = "税点")
    private BigDecimal taxPoint;

    @Excel(name = "税金")
    private BigDecimal taxAmount;

    @Excel(name = "本单利润", width = 20)
    private BigDecimal profitAmount;

    @Excel(name = "已付金额", width = 20)
    private BigDecimal paidAmount;

    @Excel(name = "应收状态")
    private String submitReceiveDesc;

    @Excel(name = "应收单号", width = 20)
    private String receiveOrderNumber;

    @Excel(name = "装卸货时间", format = "yyyy-MM-dd HH:mm", width = 20)
    private LocalDateTime loadTime;

    @Excel(name = "提空/提重时间", format = "yyyy-MM-dd HH:mm", width = 20)
    private LocalDateTime deliverGoodsTime;

    @Excel(name = "落重/还空时间", format = "yyyy-MM-dd HH:mm", width = 20)
    private LocalDateTime receiveGoodsTime;

    @Excel(name = "审核状态", width = 20)
    private String auditStatusDesc;

    @Excel(name = "运单状态")
    private String waybillStatusDesc;

    @Excel(name = "装箱地点", width = 30)
    private String containerLocation;

    @Excel(name = "装货地点", width = 30)
    private String placingLocation;

    @Excel(name = "卸货地点", width = 30)
    private String unloadingLocation;

    @Excel(name = "还箱地点", width = 30)
    private String returnContainerLocation;

    @Excel(name = "所属公司司名称", width = 30)
    private String enterpriseName;

    @Excel(name = "创建人")
    private String createUserName;

    @Excel(name = "调度人")
    private String scheduleName;

    @Excel(name = "客服")
    private String orderCreateUserName;

    @Excel(name = "创建时间", format = "yyyy-MM-dd HH:mm", width = 20)
    private LocalDateTime createTime;
}
