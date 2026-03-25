package net.lab1024.tms.admin.module.business.material.goods.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 业务资料-站点管理-编辑
 *
 * @author lihaifan
 * @date 2022/6/24 11:33
 */
@Data
public class GoodsUpdateForm extends GoodsCreateForm {

    @ApiModelProperty("货物ID")
    @NotNull(message = "货物ID不能为空")
    private Long  goodsId;
}
