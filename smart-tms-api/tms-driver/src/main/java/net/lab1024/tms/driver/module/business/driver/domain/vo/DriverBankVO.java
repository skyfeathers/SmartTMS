package net.lab1024.tms.driver.module.business.driver.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/***
 * 银行卡信息
 *
 * @author lidoudou
 * @date 2022/6/22 下午2:36
 */
@Data
public class DriverBankVO {

    @ApiModelProperty("银行ID")
    private Long bankId;

    @ApiModelProperty("银行类型")
    private String bankType;

    @ApiModelProperty("银行名称")
    private String bankName;

    @ApiModelProperty("银行账号")
    private String bankAccount;

    @ApiModelProperty("是否默认")
    private Boolean defaultFlag;

    @ApiModelProperty("支行名称")
    private String bankBranchName;

    @ApiModelProperty("开户名")
    private String accountName;
}
