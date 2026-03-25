package net.lab1024.tms.admin.module.business.reportform.shipper.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.domain.PageParam;

/**
 * 客户下单量统计
 *
 * @author lidoudou
 * @date 2022/9/20 下午5:18
 */
@Data
public class ShipperOrderQueryForm extends PageParam {

    @ApiModelProperty("客户简称、全称")
    private String keywords;

    @ApiModelProperty("年份")
    private Integer year;

    @ApiModelProperty(value = "排除状态", hidden = true)
    private Integer excludeStatus;

    @ApiModelProperty(value = "删除标识", hidden = true)
    private Boolean deletedFlag;

    @ApiModelProperty(hidden = true)
    private Long enterpriseId;
}
