package net.lab1024.tms.fixedasset.module.business.consumables.check.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.domain.PageParam;

import java.time.LocalDate;

/**
 * 分页查询耗材
 *
 * @author lidoudou
 * @date 2023/4/14 下午5:06
 */
@Data
public class ConsumablesCheckQueryForm extends PageParam {

    @ApiModelProperty(value = "创建时间 - 开始时间")
    private LocalDate startTime;

    @ApiModelProperty(value = "创建时间 - 结束时间")
    private LocalDate endTime;

    @ApiModelProperty(value = "关键字")
    private String keywords;

    @ApiModelProperty(value = "盘点位置")
    private Long locationId;

    @ApiModelProperty("所属公司")
    private Long enterpriseId;
}