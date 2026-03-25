package net.lab1024.tms.admin.module.business.oilcard.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 变更使用人
 *
 * @author lidoudou
 * @date 2022/6/30 上午11:42
 */
@Data
public class ChangeUserForm {

    @ApiModelProperty("油卡ID")
    private Long oilCardId;

    @ApiModelProperty("当前使用人")
    @NotNull(message = "当前使用人不能为空")
    private Long userId;

    @ApiModelProperty("当前使用人")
    @NotBlank(message = "当前使用人不能为空")
    private String userName;

    @ApiModelProperty("当前使用车")
    private Long useVehicleId;

    @ApiModelProperty("押金")
    private BigDecimal cashPledge;

    @ApiModelProperty("取卡时间")
    private LocalDateTime useTime;
}
