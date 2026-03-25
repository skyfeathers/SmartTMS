package net.lab1024.tms.common.module.business.material.category.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 类目 更新 DTO 类
 *
 * @author Turbolisten
 * @date 2021/1/20 16:24
 */
@Data
public class CategoryUpdateDTO extends CategoryBaseDTO {

    @ApiModelProperty("类目id")
    @NotNull(message = "类目id不能为空")
    private Long categoryId;
}
