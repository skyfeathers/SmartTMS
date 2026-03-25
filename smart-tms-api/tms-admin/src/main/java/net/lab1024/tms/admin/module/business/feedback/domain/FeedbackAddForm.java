package net.lab1024.tms.admin.module.business.feedback.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.lab1024.tms.common.common.deserializer.FileKeyVoDeserializer;
import net.lab1024.tms.common.common.serializer.FileKeyVoSerializer;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * @Author: dangxinfa
 * @Date: 2022/6/29
 */
@Data
public class FeedbackAddForm {

    @ApiModelProperty("反馈内容")
    @NotBlank(message = "反馈内容不能为空")
    @Length(max = 500, message = "反馈内容最多500字符")
    private String feedbackContent;

    @ApiModelProperty("反馈图片")
    @JsonSerialize(using = FileKeyVoSerializer.class)
    @JsonDeserialize(using = FileKeyVoDeserializer.class)
    private String feedbackAttachment;

    @ApiModelProperty(value = "创建人id", hidden = true)
    private Long createUserId;

    @ApiModelProperty(value = "创建人姓名", hidden = true)
    private String createUserName;

    @ApiModelProperty(value = "创建人类型", hidden = true)
    private Integer createUserType;

    @ApiModelProperty(value = "创建人类型描述", hidden = true)
    private String createUserTypeDesc;

    @ApiModelProperty(value = "创建人部门", hidden = true)
    private Long departmentId;
}
