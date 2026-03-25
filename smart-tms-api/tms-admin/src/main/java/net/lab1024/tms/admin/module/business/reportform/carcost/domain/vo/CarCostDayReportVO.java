package net.lab1024.tms.admin.module.business.reportform.carcost.domain.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.admin.module.business.carcost.basicinfo.domain.CarCostBasicInfoVO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 自有车基本信息统计表 VO
 *
 * @author zhaoxinyang
 * @date 2023/11/8 08:36
 */
@Data
public class CarCostDayReportVO {
    // -------------------------- 运单信息 --------------------------

    @ApiModelProperty("运单ID")
    private Long waybillId;

    @ApiModelProperty("订单ID")
    private Long orderId;

    @ApiModelProperty("年月")
//    @Excel(name = "年月", width = 20)
    private LocalDateTime businessDate;

    @ApiModelProperty("起始日期")
    private LocalDateTime createTime;

    @Excel(name = "创建时间", width = 20, orderNum = "1")
    private String createTimeStr;

    @ApiModelProperty("路线")
    @Excel(name = "路线", width = 20, orderNum = "2")
    private String routeName;

    @ApiModelProperty("箱号")
    @Excel(name = "箱号", width = 20, orderNum = "3")
    private String containerNumber;

    @ApiModelProperty("货重")
    @Excel(name = "货重", width = 20, orderNum = "4", type = 10)
    private BigDecimal totalWeight;

    @ApiModelProperty("司机ID")
    private Long driverId;

    @ApiModelProperty("司机姓名")
    @Excel(name = "司机姓名", width = 20, orderNum = "5")
    private String driverName;

    @ApiModelProperty("车辆ID")
    private Long vehicleId;

    @ApiModelProperty("车牌号")
    @Excel(name = "车牌号", width = 20, orderNum = "6")
    private String vehicleNumber;

    @ApiModelProperty("单趟应收运费（税后）")
    @Excel(name = "单趟应收运费（税后）", width = 20, orderNum = "7", type = 10)
    private BigDecimal payableAmount;

    @ApiModelProperty("其中：压车费、垫付费用等")
//    @Excel(name = "其中：压车费、垫付费用等", width = 20)
    private BigDecimal abnormalAmount;

    // -------------------------- 基本信息 --------------------------

    @ApiModelProperty("基本信息")
    private CarCostBasicInfoVO carCostBasicInfoVO;

    // -------------------------- 油卡信息 --------------------------

    @ApiModelProperty("油卡信息")
    private List<OilCardInfoVO> oilCardInfoVOList;

    // -------------------------- 现金信息 --------------------------

    @ApiModelProperty("现金信息")
    private CashInfoVO cashInfoVO;

    // -------------------------- ETC 尿素信息 --------------------------

    @ApiModelProperty("ETC")
    private CarCostDayEtcVO etcVO;

    @ApiModelProperty("尿素")
    private CarCostDayUreaVO ureaVO;

    // -------------------------- 其他费用信息 --------------------------
    @ApiModelProperty("维修费")
    @Excel(name = "维修费", width = 20, orderNum = "50", type = 10)
    private BigDecimal repairAmount;

    @ApiModelProperty("保险费")
    @Excel(name = "保险费", width = 20, orderNum = "51", type = 10)
    private BigDecimal insuranceAmount;

    @ApiModelProperty("审车费")
    @Excel(name = "审车费", width = 20, orderNum = "52", type = 10)
    private BigDecimal reviewAmount;

    @ApiModelProperty("保养费")
    @Excel(name = "保养费", width = 20, orderNum = "53", type = 10)
    private BigDecimal maintenanceAmount;

    @ApiModelProperty("备注")
    @Excel(name = "备注", width = 30, orderNum = "54")
    private String remark;
    // -------------------------- 统计信息 --------------------------

    @ApiModelProperty("油耗元/km")
    @Excel(name = "油耗元/km", width = 20, orderNum = "55", type = 10)
    private BigDecimal averageOilAmount;

    @ApiModelProperty("尿素耗元/km")
    @Excel(name = "尿素耗元/km", width = 20, orderNum = "56", type = 10)
    private BigDecimal averageUreaAmount;

    @ApiModelProperty("运费元/km")
    @Excel(name = "运费元/km", width = 20, orderNum = "57", type = 10)
    private BigDecimal averageFrightAmount;

    @ApiModelProperty("毛利")
    @Excel(name = "毛利", width = 20, orderNum = "58", type = 10)
    private BigDecimal profitAmount;

    @ApiModelProperty("用量/升")
    @Excel(name = "用量/升", width = 20, orderNum = "59", type = 10)
    private BigDecimal oilConsumption;

    @ApiModelProperty("油耗升/100km")
    @Excel(name = "油耗升/100km", width = 20, orderNum = "60", type = 10)
    private BigDecimal averageHundredKmFrightAmount;

}
