package net.lab1024.tms.admin.module.business.reportform.carcost.domain.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.print.attribute.standard.MediaSize;
import java.math.BigDecimal;

/**
 * @author lidoudou
 * @description:
 * @date 2023/11/15 09:55
 */
@Data
public class CarCostDayUreaVO {

    @ApiModelProperty("尿素支出金额")
    @Excel(name = "尿素")
    private BigDecimal ureaAmount;

    @ApiModelProperty("备注")
    private String remark;

}