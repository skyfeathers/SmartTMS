package net.lab1024.tms.common.module.support.helpdoc.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 帮助文档 目录
 *
 * @author zhuoda
 * @date 2022/08/12 20:01
 */
@Data
public class HelpDocCatalogUpdateForm extends HelpDocCatalogAddForm {

    @ApiModelProperty("id")
    @NotNull(message = "id")
    private Long helpDocCatalogId;
}
