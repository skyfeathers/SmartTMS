package net.lab1024.tms.admin.module.business.material.goods.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 业务资料-货物管理-新建
 *
 * @author lihaifan
 * @date 2022/6/24 11:32
 */
@Data
public class GoodsCreateForm {

    @ApiModelProperty("货物名称")
    @NotBlank(message = "货物名称不能为空")
    @Length(max = 200, message = "货物名称最多200字符")
    private String goodsName;

    @ApiModelProperty("货物类型[数据字典cargoTypeClassificationCode]")
    @NotBlank(message = "货物类型不能为空")
    private String goodsType;

    @ApiModelProperty("排序")
    @NotNull(message = "排序不能为空")
    private Integer sort;

    @ApiModelProperty(value = "创建人", hidden = true)
    private Long createUserId;

    @ApiModelProperty(value = "创建人", hidden = true)
    private String createUserName;
}
