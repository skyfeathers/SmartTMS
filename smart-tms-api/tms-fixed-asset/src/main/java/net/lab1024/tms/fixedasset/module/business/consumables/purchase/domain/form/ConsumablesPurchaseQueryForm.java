package net.lab1024.tms.fixedasset.module.business.consumables.purchase.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.domain.PageParam;

import java.time.LocalDate;

/**
 * 查询
 *
 * @author lidoudou
 * @date 2023/4/13 上午9:29
 */
@Data
public class ConsumablesPurchaseQueryForm extends PageParam {

    @ApiModelProperty("所属公司ID")
    private Long enterpriseId;

    @ApiModelProperty("所属位置")
    private Long locationId;

    @ApiModelProperty("关键字")
    private String keywords;

    @ApiModelProperty("资产来源")
    private String sourceId;

    @ApiModelProperty("开始时间")
    private LocalDate startTime;

    @ApiModelProperty("结束时间")
    private LocalDate endTime;

    @ApiModelProperty(hidden = true)
    private Boolean deletedFlag;
}
