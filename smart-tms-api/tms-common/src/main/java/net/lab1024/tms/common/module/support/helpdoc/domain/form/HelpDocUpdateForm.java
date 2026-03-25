package net.lab1024.tms.common.module.support.helpdoc.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 更新 帮助文档
 */
@Data
public class HelpDocUpdateForm extends HelpDocAddForm {

    @ApiModelProperty("id")
    @NotNull(message = "通知id不能为空")
    private Long helpDocId;

}
