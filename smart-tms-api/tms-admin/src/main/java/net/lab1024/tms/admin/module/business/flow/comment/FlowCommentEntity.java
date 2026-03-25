package net.lab1024.tms.admin.module.business.flow.comment;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 审批流评论
 *
 * @author lihaifan
 * @date 2022/2/10 16:08
 */
@Data
@TableName("t_flow_insance_comment")
public class FlowCommentEntity {

    @TableId(type = IdType.AUTO)
    private Long commentId;

    /**
     * 企业id
     */
    private Long enterpriseId;

    /**
     * 实例ID
     */
    private Long instanceId;

    /**
     * 实例任务ID
     */
    private Long instanceTaskId;

    /**
     * 评论人
     */
    private Long creator;

    /**
     * 接收人
     */
    private String receiver;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 删除状态
     */
    private Boolean deletedFlag;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
