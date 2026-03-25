package net.lab1024.tms.common.module.business.material.category.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.module.business.material.category.constant.CategoryTypeEnum;

import java.util.List;

/**
 * 类目 基础属性 DTO 类
 *
 * @author Turbolisten
 * @date 2021/1/20 16:17
 */
@Data
public class CategorySimpleDTO {

    @ApiModelProperty("类目id")
    private Long categoryId;

    @ApiModelProperty("类目名称")
    private String categoryName;

    @ApiModelProperty("类目层级全称")
    private String categoryFullName;

    @ApiModelProperty("分类名称列表")
    private List<String> categoryNameList;

    @ApiModelPropertyEnum(CategoryTypeEnum.class)
    private Integer categoryType;

    @ApiModelProperty("父级id")
    private Long parentId;

    @ApiModelProperty("顺序")
    private Integer sort;

    @ApiModelProperty("状态")
    private Boolean disabledFlag;
}
