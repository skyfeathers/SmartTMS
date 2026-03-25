package net.lab1024.tms.fixedasset.module.business.consumables.stock.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.domain.PageParam;

/**
 * 查询条件
 *
 * @author lidoudou
 * @date 2023/4/12 上午11:56
 */
@Data
public class ConsumablesQueryForm extends PageParam {

    @ApiModelProperty("分类ID")
    private Long categoryId;

    @ApiModelProperty("所属公司ID")
    private Long enterpriseId;

    @ApiModelProperty("关键字")
    private String keywords;

    @ApiModelProperty("所属位置ID")
    private Long locationId;

    @ApiModelProperty(hidden = true)
    private Boolean deletedFlag;
}
