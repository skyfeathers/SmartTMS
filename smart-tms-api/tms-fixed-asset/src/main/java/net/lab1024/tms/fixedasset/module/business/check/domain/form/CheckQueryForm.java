package net.lab1024.tms.fixedasset.module.business.check.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.domain.PageParam;

import java.time.LocalDate;

/**
 * 分页查询资产盘点
 *
 * @author lidoudou
 * @date 2023/3/24 上午10:43
 */
@Data
public class CheckQueryForm extends PageParam {

    @ApiModelProperty(value = "创建时间 - 开始时间")
    private LocalDate startTime;

    @ApiModelProperty(value = "创建时间 - 结束时间")
    private LocalDate endTime;

    @ApiModelProperty(value = "关键字")
    private String keywords;

    @ApiModelProperty(value = "盘点位置")
    private Long locationId;

    @ApiModelProperty(value = "所属公司",hidden = true)
    private Long enterpriseId;
}