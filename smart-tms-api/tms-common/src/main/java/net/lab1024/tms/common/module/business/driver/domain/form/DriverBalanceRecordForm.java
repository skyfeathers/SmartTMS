package net.lab1024.tms.common.module.business.driver.domain.form;

import lombok.Data;
import net.lab1024.tms.common.common.validator.enumeration.CheckEnum;
import net.lab1024.tms.common.module.business.driver.constants.DriverBalanceTradeTypeEnum;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 *
 * @author rjp
 * @date 2022年9月2日 10:28:59
 */
@Data
public class DriverBalanceRecordForm {

    /**
     * 交易类型
     * @see DriverBalanceTradeTypeEnum
     */
    @NotNull(message = "交易类型不能为空")
    @CheckEnum(value = DriverBalanceTradeTypeEnum.class,required = true,message = "交易类型错误")
    private Integer tradeType;

    /**
     * 交易内容
     */
    @NotBlank(message = "交易内容不能为空")
    private String tradeContent;

    /**
     * 关联订单号
     */
    private Long relateOrderId;


    /**
     * 备注
     */
    private String remark;
}
