package net.lab1024.tms.driver.module.business.feedback.domain.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.deserializer.FileKeyVoDeserializer;
import net.lab1024.tms.common.common.serializer.FileKeyVoSerializer;

import java.time.LocalDateTime;

/**
 * 意见反馈分页查询
 *
 * @Author: zhaoxinyang
 * @Date: 2023/09/01 14:18
 */
@Data
public class FeedbackQueryVO {

    @ApiModelProperty(value = "主键")
    private Long feedbackId;

    @ApiModelProperty(value = "反馈内容")
    private String feedbackContent;

    @ApiModelProperty("反馈图片")
    @JsonSerialize(using = FileKeyVoSerializer.class)
    @JsonDeserialize(using = FileKeyVoDeserializer.class)
    private String feedbackAttachment;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

}
