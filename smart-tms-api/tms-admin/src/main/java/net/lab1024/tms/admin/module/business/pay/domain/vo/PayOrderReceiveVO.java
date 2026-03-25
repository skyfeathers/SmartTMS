package net.lab1024.tms.admin.module.business.pay.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 收款信息
 *
 * @author yandy
 * @Date 2022-08-13
 */
@Data
public class PayOrderReceiveVO {


    private Long payOrderReceiveId;

    @ApiModelProperty("付款单id")
    private Long payOrderId;

    @ApiModelProperty("银行名称")
    private String receiveBankName;

    @ApiModelProperty("支行名称")
    private String receiveBankBranchName;

    @ApiModelProperty("开户名")
    private String receiveAccountName;

    @ApiModelProperty("银行账号")
    private String receiveBankAccount;
}
