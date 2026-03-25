package net.lab1024.tms.admin.module.business.waybill.domain.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 运单统计表
 *
 * @author lidoudou
 * @date 2022/9/25 上午9:24
 */
@Data
public class WaybillCostDetailExcelVO {

    @Excel(name = "序号", width = 20)
    private Integer index;

    @ApiModelProperty("订单所属公司")
    @Excel(name = "结算单位", width = 20)
    private String enterpriseName;

    @ApiModelProperty("货主简称")
    @Excel(name = "货主简称", width = 20)
    private String shortName;

    @ApiModelProperty("订单编号")
    @Excel(name = "订单号", orderNum = "3", width = 20)
    private String orderNo;

    @ApiModelProperty("业务类型")
    @Excel(name = "业务类型", orderNum = "5", width = 15)
    private String containerBusinessTypeName;

    @ApiModelProperty("运输方式")
    private Integer transportMode;

    @ApiModelProperty("运输方式")
    @Excel(name = "运输方式", orderNum = "5", width = 15)
    private String transportModeName;

    @Excel(name = "业务时间", orderNum = "5", width = 15, format = "yyyy-MM")
    private LocalDate businessDate;

    @ApiModelProperty("装/卸货时间")
    @Excel(name = "装/卸货时间", orderNum = "5", format = "yyyy-MM-dd HH:mm:ss", width = 20)
    private LocalDateTime loadTime;

    @ApiModelProperty("创建时间")
    @Excel(name = "创建时间", orderNum = "5", format = "yyyy-MM-dd HH:mm:ss", width = 20)
    private LocalDateTime createTime;

    @ApiModelProperty("支付状态")
    @Excel(name = "支付状态", orderNum = "5", replace = {"未申请_0", "未支付_10", "已支付_20"})
    private Integer payStatus;

    @ApiModelProperty("提箱地点")
    @Excel(name = "提箱地", orderNum = "6", width = 30)
    private String containerLocation;

    @ApiModelProperty("装货地点")
    @Excel(name = "装货地", orderNum = "7", width = 30)
    private String placingLocation;

    @ApiModelProperty("卸货地点")
    @Excel(name = "卸货地", orderNum = "8", width = 30)
    private String unloadingLocation;

    @ApiModelProperty("还箱地点")
    @Excel(name = "落箱地", orderNum = "9", width = 30)
    private String returnContainerLocation;

    @ApiModelProperty(value = "柜型")
    @Excel(name = "柜型", orderNum = "10")
    private String cabinetName;

    @ApiModelProperty(value = "重量")
    @Excel(name = "重量（吨）", orderNum = "11", type = 10, width = 15)
    private BigDecimal totalWeight;

    @ApiModelProperty("订单备注")
    @Excel(name = "订单备注", orderNum = "19", width = 20)
    private String orderRemark;

    @ApiModelProperty("运单号")
    @Excel(name = "运单号", orderNum = "4", width = 20)
    private String waybillNumber;

    @ApiModelProperty("发货日期")
    @Excel(name = "发货时间", orderNum = "11", format = "yyyy-MM-dd HH:mm:ss", width = 20)
    private LocalDateTime deliverGoodsTime;

    @ApiModelProperty("收货日期")
    @Excel(name = "收货时间", orderNum = "11", format = "yyyy-MM-dd HH:mm:ss", width = 20)
    private LocalDateTime receiveGoodsTime;

    @ApiModelProperty("车牌号")
    @Excel(name = "车牌号", orderNum = "15", width = 15)
    private String vehicleNumber;

    @ApiModelProperty("司机名称")
    @Excel(name = "司机姓名", orderNum = "16")
    private String driverName;

    @ApiModelProperty("箱号")
    @Excel(name = "箱号", orderNum = "17", width = 15)
    private String containerNumber;

    @ApiModelProperty("铅封号")
    @Excel(name = "铅封号", orderNum = "18")
    private String leadSealNumber;

    @ApiModelProperty("结算方式")
    private Integer settleMode;

    @ApiModelProperty("结算对象")
    private Integer settleType;

    @ApiModelProperty("结算对象")
    @Excel(name = "结算方式", orderNum = "29")
    private String settleModeDesc;

    @ApiModelProperty("结算对象")
    @Excel(name = "结算类型", orderNum = "29")
    private String settleTypeDesc;

    @ApiModelProperty("税金")
    @Excel(name = "税金", orderNum = "30", type = 10)
    private BigDecimal taxAmount;

    @ApiModelProperty("利润")
    @Excel(name = "利润", orderNum = "30", type = 10)
    private BigDecimal profitAmount;

    @ApiModelProperty("应付总金额")
    @Excel(name = "应付总额", orderNum = "30", type = 10)
    private BigDecimal payableAmount;

    @ApiModelProperty("合同号")
    @Excel(name = "合同号", orderNum = "31")
    private String contractCode;

    @ApiModelProperty("业务")
    @Excel(name = "业务", orderNum = "32")
    private String shipperManagerName;

    @ApiModelProperty("调度ID")
    private Long scheduleId;

    @ApiModelProperty("调度")
    @Excel(name = "调度", orderNum = "33")
    private String scheduleName;

    @ApiModelProperty("客服")
    @Excel(name = "客服", orderNum = "34")
    private String orderCreateUserName;
}
