package net.lab1024.tms.common.module.business.material.category.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.common.validator.enumeration.CheckEnum;
import net.lab1024.tms.common.module.business.material.category.constant.CategoryTypeEnum;

/**
 * 类目 层级树查询 DTO 类
 *
 * @author Turbolisten
 * @date 2021/1/20 16:17
 */
@Data
public class CategoryTreeQueryDTO {

    @ApiModelPropertyEnum(desc = "分类类型|可选", value = CategoryTypeEnum.class)
    @CheckEnum(value = CategoryTypeEnum.class, message = "分类类型错误")
    private Integer categoryType;

    @ApiModelProperty("父级类目id|可选")
    private Long parentId;
}
