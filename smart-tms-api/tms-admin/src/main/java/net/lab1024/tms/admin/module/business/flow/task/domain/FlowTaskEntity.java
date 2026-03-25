package net.lab1024.tms.admin.module.business.flow.task.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.lab1024.tms.admin.module.business.flow.constant.FlowTaskTypeEnum;

import java.time.LocalDateTime;

/**
 * [  ]
 *
 * @author yandanyang
 * @date 2021/8/16 10:36
 */
@Data
@TableName("t_flow_task")
public class FlowTaskEntity {

    @TableId(type = IdType.AUTO)
    private Long taskId;

    /**
     * 流程id
     */
    private Long flowId;


    /**
     * 企业id
     */
    private Long enterpriseId;

    /**
     * 任务名称
     */
    private String taskName;


    /**
     * 任务类型
     * {@link FlowTaskTypeEnum}
     */
    private Integer taskType;

    /**
     * 任务配置
     * FlowGatewayConfigBO -》 路由节点配置对象
     * FlowHandlerConfigBO -》 审批节点审批人配置对象
     */
    private String taskConfig;

    /**
     * 任务监听
     */
    private String taskListener;

    /**
     * 操作代码，可根据此字段判断进行什么样的操作
     */
    private String operateCode;
    /**
     * 父级任务id
     */
    private String parentIds;
    /**
     * 说明
     */
    private String taskDesc;

    private LocalDateTime updateTime;

    private LocalDateTime createTime;
}
