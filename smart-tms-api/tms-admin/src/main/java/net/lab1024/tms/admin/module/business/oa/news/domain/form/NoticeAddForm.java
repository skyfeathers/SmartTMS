package net.lab1024.tms.admin.module.business.oa.news.domain.form;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.deserializer.FileKeyVoDeserializer;
import net.lab1024.tms.common.common.swagger.ApiModelPropertyEnum;
import net.lab1024.tms.common.module.business.oa.news.constant.NoticePushTypeEnum;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 资讯 基础属性 DTO
 *
 * @author zhuoda
 * @date 2022/08/12 20:01
 */
@Data
public class NoticeAddForm {

    @ApiModelProperty("标题")
    @NotBlank(message = "标题不能为空")
    @Length(max = 200, message = "标题最多200字符")
    private String title;

    @ApiModelProperty("分类")
    @NotNull(message = "分类不能为空")
    private Long noticeTypeId;

    @ApiModelPropertyEnum(value = NoticePushTypeEnum.class, desc = "推送类型")
    @NotNull(message = "推送类型不能为空")
    private Long noticePushType;

    @ApiModelProperty("是否全部可见")
    @NotNull(message = "是否全部可见不能为空")
    private Boolean allVisibleFlag;

    @ApiModelProperty("是否定时发布")
    @NotNull(message = "是否定时发布不能为空")
    private Boolean scheduledPublishFlag;

    @ApiModelProperty("发布时间")
    @NotNull(message = "发布时间不能为空")
    private LocalDateTime publishTime;

    @ApiModelProperty("纯文本内容")
    @NotNull(message = "文本内容不能为空")
    private String contentText;

    @ApiModelProperty("html内容")
    @NotNull(message = "html内容不能为空")
    private String contentHtml;

    @ApiModelProperty("附件,多个英文逗号分隔|可选")
    @Length(max = 1000, message = "最多1000字符")
    @JsonDeserialize(using = FileKeyVoDeserializer.class)
    private String attachment;

    @ApiModelProperty("作者")
    @NotBlank(message = "作者不能为空")
    private String author;

    @ApiModelProperty("来源")
    @NotBlank(message = "标题不能为空")
    private String source;

    @ApiModelProperty("文号")
    private String documentNumber;

    @ApiModelProperty(hidden = true)
    private Long createUserId;

    @ApiModelProperty("可见范围设置|可选")
    @Valid
    private List<NoticeVisibleRangeForm> visibleRangeList;

    @ApiModelProperty(hidden = true)
    private Long enterpriseId;
}
