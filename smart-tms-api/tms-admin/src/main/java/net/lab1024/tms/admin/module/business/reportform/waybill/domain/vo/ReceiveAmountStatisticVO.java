package net.lab1024.tms.admin.module.business.reportform.waybill.domain.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author lidoudou
 * @description:
 * @date 2023/6/29 上午9:11
 */
@Data
public class ReceiveAmountStatisticVO {

    @ApiModelProperty("员工姓名")
    @Excel(name = "员工姓名", width = 20)
    private String employeeName;

    @ApiModelProperty("应收金额")
    @Excel(name = "应收金额", width = 20)
    private BigDecimal receiveAmount;

    @ApiModelProperty("应收占比")
    @Excel(name = "应收占比", width = 20)
    private BigDecimal receiveAmountRate;

}