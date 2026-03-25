package net.lab1024.tms.admin.module.business.flow.comment;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 审批评论
 *
 * @author lihaifan
 * @date 2022/2/10 17:22
 */
@Data
public class FlowCommentVO {

    @ApiModelProperty("实例ID")
    private Long instanceId;

    @ApiModelProperty("实例任务ID")
    private Long instanceTaskId;

    @ApiModelProperty("评论内容")
    private String content;

    @ApiModelProperty("评论人")
    private Long creator;

    @ApiModelProperty("评论人名称")
    private String creatorName;

    @ApiModelProperty("评论时间")
    private LocalDateTime createTime;
}
