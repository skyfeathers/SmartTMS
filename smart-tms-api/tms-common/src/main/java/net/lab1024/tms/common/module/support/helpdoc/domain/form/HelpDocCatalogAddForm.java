package net.lab1024.tms.common.module.support.helpdoc.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * 帮助文档 目录
 *
 * @author zhuoda
 * @date 2022/08/12 20:01
 */
@Data
public class HelpDocCatalogAddForm {

    @ApiModelProperty("名称")
    @NotBlank(message = "名称不能为空")
    @Length(max = 200, message = "名称最多200字符")
    private String name;

    @ApiModelProperty("父级")
    private Long parentId;

    @ApiModelProperty("排序")
    private Integer sort;
}
