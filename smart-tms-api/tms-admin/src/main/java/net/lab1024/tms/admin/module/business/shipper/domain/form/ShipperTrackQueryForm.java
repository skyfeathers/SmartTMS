package net.lab1024.tms.admin.module.business.shipper.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.domain.PageParam;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * [  ]
 *
 * @author yandanyang
 * @date 2020/10/28 19:38
 */
@Data
public class ShipperTrackQueryForm extends PageParam {

    @ApiModelProperty("货主id")
    @NotNull(message = "货主ID不能为空")
    private Long shipperId;

    @ApiModelProperty(value = "跟进人Id",hidden = true)
    private Long employeeId;

    @ApiModelProperty("关键字")
    private String keywords;

    @ApiModelProperty(value = "开始时间", example = "2020-01-01")
    private LocalDate startTime;

    @ApiModelProperty(value = "结束时间", example = "2020-02-01")
    private LocalDate endTime;
}
