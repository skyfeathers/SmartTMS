package net.lab1024.tms.fixedasset.module.business.report.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.domain.PageParam;

/**
 * 查询折扣明细
 *
 * @author lidoudou
 * @date 2023/3/29 下午1:50
 */
@Data
public class DepreciationStatisticQueryForm extends PageParam {

    @ApiModelProperty(value = "关键字")
    private String keywords;

    @ApiModelProperty(value = "所属公司",hidden = true)
    private Long enterpriseId;

    @ApiModelProperty(value = "所属分类")
    private Long categoryId;

    @ApiModelProperty(hidden = true)
    private Boolean deletedFlag;
}
