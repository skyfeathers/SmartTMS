package net.lab1024.tms.admin.module.business.material.yard.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 业务资料-堆场管理-编辑
 *
 * @author lihaifan
 * @date 2022/6/24 11:33
 */
@Data
public class YardUpdateForm extends YardCreateForm {

    @ApiModelProperty("堆场ID")
    @NotNull(message = "堆场ID不能为空")
    private Long yardId;
}
