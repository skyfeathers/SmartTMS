package net.lab1024.tms.admin.module.business.etc.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 充值
 *
 * @author lidoudou
 * @date 2022/6/30 上午11:40
 */
@Data
public class EtcRechargeForm {

    @ApiModelProperty("ETC ID")
    private Long etcId;

    @ApiModelProperty("充值金额")
    private BigDecimal rechargeAmount;

    @ApiModelProperty("充值人")
    private String rechargeUserName;

    @ApiModelProperty(value = "充值人", example = "2022-02-01 10:29:34")
    private LocalDateTime rechargeTime;

    @ApiModelProperty("备注")
    private String remark;
}
