package net.lab1024.tms.fixedasset.module.business.consumables.check.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 耗材盘点完成
 *
 * @author lidoudou
 * @date 2023/4/14 下午5:07
 */
@Data
public class ConsumablesCheckCompleteForm {

    @ApiModelProperty("盘点ID")
    @NotNull(message = "盘点ID不能为空")
    private Long checkId;

    @ApiModelProperty("盘点完成时间")
    @NotNull(message = "盘点完成时间不能为空")
    private LocalDateTime completeTime;
}
