package net.lab1024.tms.admin.module.business.material.cost.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.domain.PageParam;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.module.business.material.cost.constants.CostItemCategoryEnum;
import net.lab1024.tms.common.module.business.material.cost.constants.CostItemTypeEnum;
import org.hibernate.validator.constraints.Length;
/**
 * @author yandy
 * @description:
 * @date 2022/8/12 10:12 上午
 */
@Data
public class CostItemQueryForm extends PageParam {


    @ApiModelProperty("关键字")
    @Length(max = 200, message = "关键字最多200字符")
    private String keywords;

    @ApiModelPropertyEnum(value = CostItemTypeEnum.class, desc = "类型")
    private Integer type;

    @ApiModelPropertyEnum(value = CostItemCategoryEnum.class, desc = "分类")
    private Integer category;

    @ApiModelProperty(value = "删除状态", hidden = true)
    private Boolean deletedFlag;
}