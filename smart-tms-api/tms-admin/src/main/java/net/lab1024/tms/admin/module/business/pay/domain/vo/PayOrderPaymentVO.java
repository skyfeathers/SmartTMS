package net.lab1024.tms.admin.module.business.pay.domain.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.serializer.FileKeyVoSerializer;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 付款信息
 * @author yandy
 * @Date 2022-08-13
 */
@Data
public class PayOrderPaymentVO {

    private Long payOrderPaymentId;

    @ApiModelProperty("付款单id")
    private Long payOrderId;

    @ApiModelProperty("银行名称")
    private String payBankName;

    @ApiModelProperty("开户名")
    private String payAccountName;

    @ApiModelProperty("银行账号")
    private String payBankAccount;

    @ApiModelProperty("创建人id")
    private Long createUserId;

    @ApiModelProperty("创建人名称")
    private String createUserName;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

}
