package net.lab1024.tms.common.module.support.table.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author zhuoda
 * @date 2022-08-20 17:35
 */

@Data
public class TableColumnItemForm {

    @NotEmpty(message = "列不能为空")
    @ApiModelProperty("字段")
    private String columnKey;

    @ApiModelProperty("宽度")
    private Integer width;

    @NotNull(message = "显示不能为空")
    @ApiModelProperty("是否显示")
    private Boolean showFlag;

    @NotNull(message = "排序不能为空")
    @ApiModelProperty("排序")
    private Integer sort;


}
