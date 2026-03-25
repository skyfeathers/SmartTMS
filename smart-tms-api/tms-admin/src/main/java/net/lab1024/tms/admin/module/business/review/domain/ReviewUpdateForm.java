package net.lab1024.tms.admin.module.business.review.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 修改审车信息
 *
 * @author zhaoxinyang
 * @date 2023/10/18 10:47
 */
@Data
public class ReviewUpdateForm extends ReviewAddForm {

    @ApiModelProperty("审车表ID")
    @NotNull(message = "审车表ID不能为空")
    private Long reviewId;

}