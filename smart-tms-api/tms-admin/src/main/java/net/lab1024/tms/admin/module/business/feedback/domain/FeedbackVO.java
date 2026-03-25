package net.lab1024.tms.admin.module.business.feedback.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.deserializer.FileKeyVoDeserializer;
import net.lab1024.tms.common.common.serializer.FileKeyVoSerializer;

import java.time.LocalDateTime;

/**
 * 意见反馈
 *
 * @Author: dangxinfa
 * @Date: 2022/6/28
 */
@Data
public class FeedbackVO {

    @ApiModelProperty(value = "主键")
    private Long feedbackId;

    @ApiModelProperty(value = "反馈内容")
    private String feedbackContent;

    @ApiModelProperty("反馈图片")
    @JsonSerialize(using = FileKeyVoSerializer.class)
    @JsonDeserialize(using = FileKeyVoDeserializer.class)
    private String feedbackAttachment;

    @ApiModelProperty(value = "创建人id")
    private Long createId;

    @ApiModelProperty(value = "创建人姓名")
    private String createName;

    @ApiModelProperty(value = "部门id")
    private Long departmentId;

    @ApiModelProperty(value = "部门全路径")
    private String departmentName;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;
}