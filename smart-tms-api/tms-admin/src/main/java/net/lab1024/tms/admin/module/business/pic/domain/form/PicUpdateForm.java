package net.lab1024.tms.admin.module.business.pic.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author zhuoda
 * @Date 2021/1/22
 */
@Data
public class PicUpdateForm extends PicAddForm {

    @ApiModelProperty("id")
    @NotNull(message = "请选择图片")
    private Long picId;
}
