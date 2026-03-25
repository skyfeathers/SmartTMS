package net.lab1024.tms.admin.module.business.oa.news.domain.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.serializer.FileKeyVoSerializer;

import java.util.List;

/**
 * 用于更新 【通知、公告】 的 VO 对象
 *
 * @author zhuoda
 * @date 2022/08/12 20:11:11
 */
@Data
public class NoticeUpdateFormVO extends NoticeVO{

    @ApiModelProperty("纯文本内容")
    private String contentText;

    @ApiModelProperty("html内容")
    private String contentHtml;

    @ApiModelProperty("附件")
    @JsonSerialize(using = FileKeyVoSerializer.class)
    private String attachment;

    @ApiModelProperty("可见范围")
    private List<NoticeVisibleRangeVO> visibleRangeList;

}
