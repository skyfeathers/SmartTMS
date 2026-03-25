package net.lab1024.tms.common.module.support.helpdoc.domain.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.serializer.FileKeyVoSerializer;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class HelpDocDetailVO {

    @ApiModelProperty("id")
    private Long helpDocId;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("分类")
    private Long helpDocCatalogId;

    @ApiModelProperty("分类名称")
    private Long helpDocCatalogName;

    @ApiModelProperty("纯文本内容")
    private String contentText;

    @ApiModelProperty("html内容")
    private String contentHtml;

    @ApiModelProperty("附件")
    @JsonSerialize(using = FileKeyVoSerializer.class)
    private String attachment;

    @ApiModelProperty("作者")
    @NotBlank(message = "作者不能为空")
    private String author;

    @ApiModelProperty("页面浏览量")
    private Integer pageViewCount;

    @ApiModelProperty("用户浏览量")
    private Integer userViewCount;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty("关联项目")
    private List<HelpDocRelationVO> relationList;

}
