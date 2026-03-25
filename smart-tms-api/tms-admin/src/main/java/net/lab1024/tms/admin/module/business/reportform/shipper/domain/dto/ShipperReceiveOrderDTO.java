package net.lab1024.tms.admin.module.business.reportform.shipper.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 根据货主查询已提交核算金额
 *
 * @author zhaoxinyang
 * @date 2023/12/13 14:01
 */
@Data
public class ShipperReceiveOrderDTO {

    @ApiModelProperty("运单ID")
    private Long waybillId;

    @ApiModelProperty("金额")
    private BigDecimal receiveAmount;

}
