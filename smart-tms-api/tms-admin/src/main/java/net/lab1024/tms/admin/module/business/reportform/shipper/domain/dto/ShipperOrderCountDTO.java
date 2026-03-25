package net.lab1024.tms.admin.module.business.reportform.shipper.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 根据货主查询取消订单数
 *
 * @author zhaoxinyang
 * @date 2023/12/13 14:01
 */
@Data
public class ShipperOrderCountDTO {

    @ApiModelProperty("货主ID")
    private Long shipperId;

    @ApiModelProperty("订单数")
    private Integer orderCount;

}
