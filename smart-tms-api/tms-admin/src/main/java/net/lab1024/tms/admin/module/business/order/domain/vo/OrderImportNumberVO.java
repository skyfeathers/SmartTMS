package net.lab1024.tms.admin.module.business.order.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *
 *
 * @author lidoudou
 * @date 2023/2/14 上午10:07
 */
@Data
public class OrderImportNumberVO extends OrderImportResultVO {

    @ApiModelProperty("订单编号")
    private String orderNo;

    @ApiModelProperty("运单号")
    private String waybillNumber;
}
