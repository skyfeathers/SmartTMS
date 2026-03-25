package net.lab1024.tms.admin.module.business.reportform.waybill.domain.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 油卡比例表
 *
 * @author lidoudou
 * @date 2023/3/9 上午8:59
 */
@Data
public class EmployeeOilCardRateVO {

    @ApiModelProperty("员工姓名")
    @Excel(name = "姓名")
    private String employeeName;

    @ApiModelProperty("应付金额")
    @Excel(name = "外调车运费成本", width = 20)
    private BigDecimal payableAmount;

    @ApiModelProperty("油卡金额")
    @Excel(name = "分配外调车油卡", width = 20)
    private BigDecimal oilCardAmount;

    @ApiModelProperty("油卡比例")
    @Excel(name = "油卡比例", width = 20)
    private BigDecimal oilCardRate;
}
