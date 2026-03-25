package net.lab1024.tms.common.module.support.helpdoc.domain.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.domain.PageParam;

import java.time.LocalDate;

/**
 * 帮助文档 分页查询
 *
 * @author: zhuoda
 * @date: 2022/08/12 20:24
 */
@Data
public class HelpDocQueryForm extends PageParam {

    @ApiModelProperty("分类")
    private Long helpDocCatalogId;

    @ApiModelProperty("标题")
    private String keywords;

    @ApiModelProperty("创建-开始时间")
    private LocalDate createTimeBegin;

    @ApiModelProperty("创建-截止时间")
    private LocalDate createTimeEnd;

}
