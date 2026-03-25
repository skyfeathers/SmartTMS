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
public class ShipperOrderWaybillDTO {

    @ApiModelProperty("运单ID")
    private Long waybillId;

    @ApiModelProperty("货主ID")
    private Long shipperId;

}
