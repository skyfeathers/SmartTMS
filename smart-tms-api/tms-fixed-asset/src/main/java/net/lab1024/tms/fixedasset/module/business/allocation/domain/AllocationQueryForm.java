package net.lab1024.tms.fixedasset.module.business.allocation.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.domain.PageParam;

import java.time.LocalDate;

/**
 * 分页查询调拨
 *
 * @author lidoudou
 * @date 2023/3/20 下午5:43
 */
@Data
public class AllocationQueryForm extends PageParam {

    @ApiModelProperty(value = "使用时间 - 开始时间")
    private LocalDate startTime;

    @ApiModelProperty(value = "使用时间 - 结束时间")
    private LocalDate endTime;

    @ApiModelProperty(value = "关键字")
    private String keywords;

    @ApiModelProperty(value = "调出位置")
    private Long fromLocationId;

    @ApiModelProperty(value = "调入位置")
    private Long toLocationId;

    @ApiModelProperty(hidden = true)
    private Long enterpriseId;
}