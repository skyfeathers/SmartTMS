package net.lab1024.tms.common.module.support.helpdoc.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 帮助文档的 目录
 *
 * @author: zhuoda
 * @date: 2022/08/12 20:26
 */
@Data
public class HelpDocCatalogVO {

    @ApiModelProperty("帮助文档目录id")
    private Long helpDocCatalogId;

    @ApiModelProperty("帮助文档目录-名称")
    private String name;

    @ApiModelProperty("帮助文档目录-父级id")
    private Long parentId;

    @ApiModelProperty("帮助文档目录-排序")
    private Integer sort;

}
