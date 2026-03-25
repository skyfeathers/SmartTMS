package net.lab1024.tms.admin.module.business.receive.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 已核销金额统计
 *
 * @author lidoudou
 * @date 2022/10/21 下午8:31
 */
@Data
public class ReceiveOrderVerificationAmountDTO {

    @ApiModelProperty("应收账款ID")
    private Long receiveOrderId;

    @ApiModelProperty("已核销金额")
    private BigDecimal verificationAmount;
}
