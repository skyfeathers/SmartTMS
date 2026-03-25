package net.lab1024.tms.admin.module.business.receive.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 待收款的信息
 *
 * @author lidoudou
 * @date 2022/8/24 下午3:53
 */
@Data
public class WaitReceiveOrderVO extends ReceiveOrderVO {

    @ApiModelProperty("运单ID")
    private Long waybillId;

    @ApiModelProperty("运单号")
    private String waybillNumber;

}
