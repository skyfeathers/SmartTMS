package net.lab1024.tms.admin.module.business.reportform.waybill.domain.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.admin.module.business.reportform.employee.domain.dto.WaybillEmployeeAmountDTO;

import java.math.BigDecimal;

/**
 * 客服、业务负责人收入成本明细
 *
 * @author lidoudou
 * @date 2023/3/7 下午5:21
 */
@Data
public class WaybillEmployeeAmountStatisticVO extends WaybillEmployeeAmountDTO {

    @ApiModelProperty("员工姓名")
    @Excel(name = "员工姓名", orderNum = "1", width = 20)
    private String employeeName;

    @ApiModelProperty("毛利润")
    @Excel(name = "毛利润", orderNum = "5", width = 20)
    private BigDecimal profitAmount;

    @ApiModelProperty("毛利率")
    @Excel(name = "毛利率", orderNum = "6", width = 20)
    private BigDecimal profitRate;
}
