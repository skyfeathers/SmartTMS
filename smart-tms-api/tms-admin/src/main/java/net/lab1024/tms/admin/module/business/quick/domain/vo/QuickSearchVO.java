package net.lab1024.tms.admin.module.business.quick.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.admin.module.business.quick.QuickSearchTypeEnum;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;

/**
 * @author yandy
 * @description:
 * @date 2024/10/25 10:09 上午
 */
@Data
public class QuickSearchVO {

    @ApiModelProperty("是否查询到数据")
    private Boolean successFlag;

    @ApiModelPropertyEnum(value = QuickSearchTypeEnum.class, desc = "查询类型")
    private Integer searchType;

    @ApiModelProperty("对应数据模块id")
    private Long id;

}