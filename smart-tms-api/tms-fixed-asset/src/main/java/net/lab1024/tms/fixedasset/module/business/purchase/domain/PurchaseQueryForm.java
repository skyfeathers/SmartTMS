package net.lab1024.tms.fixedasset.module.business.purchase.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.domain.PageParam;

import java.time.LocalDate;

/**
 * 分页查询资产采购
 *
 * @author lidoudou
 * @date 2023/3/20 上午9:11
 */
@Data
public class PurchaseQueryForm extends PageParam {

    @ApiModelProperty(value = "创建时间 - 开始时间")
    private LocalDate startTime;

    @ApiModelProperty(value = "创建时间 - 结束时间")
    private LocalDate endTime;

    @ApiModelProperty(value = "关键字")
    private String keywords;

    @ApiModelProperty(value = "所属分类")
    private Long categoryId;

    @ApiModelProperty(value = "来源")
    private String sourceId;

    @ApiModelProperty(value = "所属公司",hidden = true)
    private Long enterpriseId;

}