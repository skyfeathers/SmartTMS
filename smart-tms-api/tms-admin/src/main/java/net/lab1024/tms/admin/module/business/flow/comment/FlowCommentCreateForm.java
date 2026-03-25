package net.lab1024.tms.admin.module.business.flow.comment;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 添加审批评论
 *
 * @author lihaifan
 * @date 2022/2/10 16:21
 */
@Data
public class FlowCommentCreateForm {

    /**
     * 实例ID
     */
    @ApiModelProperty("实例ID")
    @NotNull(message = "实例ID不能为空")
    private Long instanceId;

    /**
     * 当前任务ID
     */
    @ApiModelProperty("当前任务ID")
    @NotNull(message = "当前任务ID不能为空")
    private Long currentTaskId;

    /**
     * 评论内容
     */
    @ApiModelProperty("评论内容")
    @NotBlank(message = "评论内容不能为空")
    @Length(max = 200, message = "评论内容最多200字符")
    private String content;

    @ApiModelProperty(hidden = true)
    private Long operatorId;

    @ApiModelProperty(hidden = true)
    private String operatorName;

    @ApiModelProperty(value = "企业id",hidden = true)
    private Long enterpriseId;
}
