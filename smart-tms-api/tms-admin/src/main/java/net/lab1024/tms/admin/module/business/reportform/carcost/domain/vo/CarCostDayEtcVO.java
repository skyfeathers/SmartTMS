package net.lab1024.tms.admin.module.business.reportform.carcost.domain.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author lidoudou
 * @description:
 * @date 2023/11/15 09:55
 */
@Data
public class CarCostDayEtcVO {

    @ApiModelProperty("ETC金额")
    @Excel(name = "ETC金额")
    private BigDecimal etcAmount;

    @ApiModelProperty("备注")
    private String remark;

}