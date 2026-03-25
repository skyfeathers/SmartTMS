package net.lab1024.tms.admin.module.business.reportform.shipper.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 货主每月下单量统计
 *
 * @author lidoudou
 * @date 2022/9/20 下午5:23
 */
@Data
public class ShipperCountMonthVO {

    @ApiModelProperty(value = "货主ID")
    private Long shipperId;

    @ApiModelProperty(value = "下单月份", example = "2022-09")
    private String month;

    @ApiModelProperty("每月数量")
    private Integer count;
}
