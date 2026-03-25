package net.lab1024.tms.admin.module.business.oilcard.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.module.business.oilcard.constant.OilCardBalanceRecordTypeEnum;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author yandy
 * @description:
 * @date 2022/8/19 3:52 下午
 */
@Data
public class OilCardBalanceChangeForm {

    @ApiModelProperty("油卡ID")
    @NotNull(message = "油卡ID不能为空")
    private Long oilCardId;

    @ApiModelProperty("变动金额")
    @NotNull(message = "金额不能为空")
    private BigDecimal changeAmount;

    @ApiModelProperty(value = "交易时间")
    private LocalDateTime transactionTime;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelPropertyEnum(desc = "交易类型", value = OilCardBalanceRecordTypeEnum.class, hidden = true)
    private Integer recordType;

    @ApiModelProperty(value = "操作人", hidden = true)
    private Long operatorId;

    @ApiModelProperty(value = "操作人", hidden = true)
    private String operatorName;

    @ApiModelProperty(value = "是否禁用操作的卡", hidden = true)
    private Boolean disabledOperateCardFlag;
}