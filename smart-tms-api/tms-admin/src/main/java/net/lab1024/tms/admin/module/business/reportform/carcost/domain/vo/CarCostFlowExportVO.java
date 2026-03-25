package net.lab1024.tms.admin.module.business.reportform.carcost.domain.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 自有车-费用流水 VO
 *
 * @author zhaoxinyang
 * @date 2023/10/23 16:04
 */
@Data
public class CarCostFlowExportVO {

    @ApiModelProperty("运单ID")
    private Long waybillId;

    @ApiModelProperty("年")
    @Excel(name = "年", width = 10)
    private String year;

    @ApiModelProperty("序号")
    @Excel(name = "序号", width = 10)
    private Integer serialNo;

    @ApiModelProperty("车辆ID")
    private Long vehicleId;

    @ApiModelProperty("车号")
    @Excel(name = "车号", width = 15)
    private String vehicleNumber;


    @ApiModelProperty("司机ID")
    private Long driverId;

    @ApiModelProperty("司机")
    @Excel(name = "司机", width = 10)
    private String driverName;

    @ApiModelProperty("月份")
    @Excel(name = "月份", width = 10)
    private String month;

    @ApiModelProperty("货主ID")
    private Long shipperId;

    @ApiModelProperty("分类")
    @Excel(name = "分类", width = 20)
    private String categoryName;

    @ApiModelProperty("日期")
    @Excel(name = "日期", width = 10)
    private String date;

    @ApiModelProperty("摘要")
    @Excel(name = "摘要", width = 30)
    private String summary;

    @ApiModelProperty("支出金额")
    @Excel(name = "支出金额", width = 15)
    private BigDecimal payAmount;

    @ApiModelProperty("应收运费")
    @Excel(name = "应收运费", width = 15)
    private BigDecimal receiveAmount;

    @ApiModelProperty("备注")
    @Excel(name = "备注", width = 20)
    private String remark;

    @ApiModelProperty("结算月份")
    @Excel(name = "结算月份", width = 20)
    private Integer settlementMonth;

}
