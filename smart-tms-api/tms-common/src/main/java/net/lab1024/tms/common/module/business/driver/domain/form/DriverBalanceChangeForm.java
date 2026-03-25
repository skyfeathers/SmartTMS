
package net.lab1024.tms.common.module.business.driver.domain.form;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 *
 * @author rjp
 * @date 2022年9月2日 10:28:59
 */
@Data
public class DriverBalanceChangeForm {

    @NotNull(message = "司机id不能为空")
    private Long driverId;

    @NotNull(message = "企业id不能为空")
    private Long enterpriseId;

    @NotNull(message = "变动金额不能为空")
    @DecimalMin(value = "0", message = "变动金额不能为负数")
    private BigDecimal changeAmount;

    @NotNull(message = "收入标识不能为空")
    private Boolean incomeFlag;

    @Valid
    @NotNull(message = "交易流水信息不能为空")
    private DriverBalanceRecordForm recordForm;
}
