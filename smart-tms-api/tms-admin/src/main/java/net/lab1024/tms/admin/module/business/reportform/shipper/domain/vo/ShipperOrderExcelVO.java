package net.lab1024.tms.admin.module.business.reportform.shipper.domain.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 统计货主每月下单量
 *
 * @author lidoudou
 * @date 2022/9/20 下午5:29
 */
@Data
public class ShipperOrderExcelVO {

    @ApiModelProperty("货主id")
    private Long shipperId;

    @ApiModelProperty("货主名称")
    private String consignor;

    @ApiModelProperty("货主简称")
    @Excel(name = "货主简称", width = 20)
    private String shortName;

    @Excel(name = "一月", type = 10)
    private Integer januaryCount;

    @Excel(name = "二月", type = 10)
    private Integer februaryCount;

    @Excel(name = "三月", type = 10)
    private Integer marchCount;

    @Excel(name = "四月", type = 10)
    private Integer aprilCount;

    @Excel(name = "五月", type = 10)
    private Integer mayCount;

    @Excel(name = "六月", type = 10)
    private Integer juneCount;

    @Excel(name = "七月", type = 10)
    private Integer julyCount;

    @Excel(name = "八月", type = 10)
    private Integer augustCount;

    @Excel(name = "九月", type = 10)
    private Integer septemberCount;

    @Excel(name = "十月", type = 10)
    private Integer octoberCount;

    @Excel(name = "十一月", type = 10)
    private Integer novemberCount;

    @Excel(name = "十二月", type = 10)
    private Integer decemberCount;

}
