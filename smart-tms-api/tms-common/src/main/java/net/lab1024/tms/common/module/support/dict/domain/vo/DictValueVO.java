package net.lab1024.tms.common.module.support.dict.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yandy
 * @description:
 * @date 2022/5/26 12:20 上午
 */
@Data
public class DictValueVO {

    @ApiModelProperty("valueId")
    private Long dictValueId;

    @ApiModelProperty("dictKeyId")
    private Long dictKeyId;

    @ApiModelProperty("编码")
    private String valueCode;

    @ApiModelProperty("名称")
    private String valueName;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("备注")
    private String remark;
}