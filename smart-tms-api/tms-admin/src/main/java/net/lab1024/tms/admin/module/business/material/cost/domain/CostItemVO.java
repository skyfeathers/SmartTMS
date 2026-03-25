package net.lab1024.tms.admin.module.business.material.cost.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.module.business.material.cost.constants.CostItemCategoryEnum;
import net.lab1024.tms.common.module.business.material.cost.constants.CostItemTypeEnum;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * @author zhuoda
 * @Date 2022-07-18
 */
@Data
public class CostItemVO {

    private Long costItemId;

    @ApiModelProperty("名称")
    @NotBlank(message = "名称不能为空")
    private String name;

    @ApiModelPropertyEnum(CostItemTypeEnum.class)
    private Integer type;

    @ApiModelPropertyEnum(CostItemCategoryEnum.class)
    private Integer category;

    @ApiModelProperty("排序")
    private Integer seq;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("创建人ID")
    private Long createUserId;

    @ApiModelProperty("创建人名称")
    private String createUserName;

    private LocalDateTime createTime;

}
