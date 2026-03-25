package net.lab1024.tms.admin.module.business.receive.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 获取批量核销展示的金额信息
 *
 * @author lidoudou
 * @date 2022/10/22 下午4:22
 */
@Data
public class ReceiveOrderVerificationAmountVO {

    @ApiModelProperty("总金额")
    private BigDecimal totalAmount;

    @ApiModelProperty("核销金额")
    private BigDecimal verificationAmount;

    @ApiModelProperty("未核销金额")
    private BigDecimal unpaidAmount;
}
