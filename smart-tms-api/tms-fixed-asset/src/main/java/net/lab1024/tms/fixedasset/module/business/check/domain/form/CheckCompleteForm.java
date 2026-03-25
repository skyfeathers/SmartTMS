package net.lab1024.tms.fixedasset.module.business.check.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 盘点完成
 *
 * @author lidoudou
 * @date 2023/3/27 上午8:48
 */
@Data
public class CheckCompleteForm {

    @ApiModelProperty("盘点ID")
    @NotNull(message = "盘点ID不能为空")
    private Long checkId;

    @ApiModelProperty("盘点完成时间")
    @NotNull(message = "盘点完成时间不能为空")
    private LocalDateTime completeTime;
}
