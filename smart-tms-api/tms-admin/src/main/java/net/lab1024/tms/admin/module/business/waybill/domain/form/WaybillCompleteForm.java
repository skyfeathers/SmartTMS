package net.lab1024.tms.admin.module.business.waybill.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author yandy
 * @description:
 * @date 2022/8/25 10:30 上午
 */
@Data
public class WaybillCompleteForm {

    @ApiModelProperty("运单id")
    @NotEmpty(message = "运单id不能为空")
    private List<Long> waybillIdList;

    @ApiModelProperty("完成时间")
    @NotNull(message = "完成时间不能为空")
    private LocalDateTime completeTime;
}